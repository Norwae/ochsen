package de.codecentric.ochsen.core.parser

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

class JsonTreeRepository(
        private val rootPath: Path,
        private val json: ObjectMapper,
        private val yaml: ObjectMapper) {

    private val fileTrees = mutableMapOf<Path, JsonNode>()

    private fun splitPath(path: Path): Pair<Path, List<String>> {
        var root = rootPath
        var remainingSegments = path.toList()
        for (segment in path) {
            if (segment.toString() == "#") {
                remainingSegments = remainingSegments.drop(1)
                break
            }

            val resolved = root.resolve(segment)

            if (!Files.exists(resolved)) {
                throw FileNotFoundException("$resolved could not be found")
            }

            root = resolved
            remainingSegments = remainingSegments.drop(1)
        }

        return root to remainingSegments.map(Path::toString)
    }

    fun <T: Any> get(path: Path, clazz: KClass<T>): T {
        val (file, internalPath) = splitPath(path)
        val subtree = walk(parseFileAt(emptySet(), file), internalPath)

        return json.treeToValue(subtree, clazz.java)
    }

    private fun parseFileAt(pending: Set<Path>, file: Path): JsonNode {
        check(!pending.contains(file)) {
            "Recursive expansion requested, $file is already being loaded (pending=$pending)"
        }

        val mapper =
                if (file.fileName.toString().endsWith(".yaml")) yaml else json

        val absolute = file.toAbsolutePath().normalize()
        val rawTree = fileTrees.computeIfAbsent(absolute){
            mapper.readTree(absolute.toFile())
        }

        val resolved = resolveReferences(pending, file, rawTree, rawTree)
        fileTrees[absolute] = resolved

        return resolved
    }

    private fun resolveReferences(pending: Set<Path>, current: Path, tree: JsonNode, root: JsonNode): JsonNode {
        return when  {
            tree.isObject && tree.hasNonNull("\$ref") ->
                resolveReferences(
                        pending,
                        current,
                        replaceReference(pending, current, tree.get("\$ref").textValue(), root),
                        root)
            tree.isObject -> {
                val newObj = json.createObjectNode()
                for (field in tree.fields()) {
                    newObj[field.key] = resolveReferences(pending, current, field.value, root)
                }
                newObj
            }
            else -> tree
        }
    }

    private fun walk(node: JsonNode, segments: List<String>): JsonNode {
        var cursor = node

        for (segment in segments) {
            cursor = cursor[segment]
        }

        return cursor
    }

    private fun replaceReference(pending: Set<Path>, current: Path, refExpression: String, root: JsonNode): JsonNode {
        val (path, filePath) = splitPath(Paths.get(refExpression))

        val documentRoot = if (path == rootPath) root else parseFileAt(pending + current, path)

        return walk(documentRoot, filePath)
    }

}
