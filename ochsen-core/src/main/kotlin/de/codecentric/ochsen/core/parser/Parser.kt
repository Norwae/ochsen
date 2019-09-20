package de.codecentric.ochsen.core.parser

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import de.codecentric.ochsen.core.model.*
import java.nio.file.Path
import java.nio.file.Paths

class Parser(rootPath: Path) {
    private val repository: JsonTreeRepository

    init {
        fun setupObjectMapper(mapper: ObjectMapper) {
            mapper.registerModule(KotlinModule())
            mapper.registerModule(JavaTimeModule())
            mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        val jsonParser = ObjectMapper()
        val yamlParser = ObjectMapper(YAMLFactory())

        setupObjectMapper(jsonParser)
        setupObjectMapper(yamlParser)
        repository = JsonTreeRepository(rootPath, jsonParser, yamlParser)
    }


    fun parse(relativePath: String): OpenAPI3Document {
        return repository.get(Paths.get(relativePath), OpenAPI3Document::class)
    }

}