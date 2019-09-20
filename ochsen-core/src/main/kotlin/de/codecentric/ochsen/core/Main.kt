package de.codecentric.ochsen.core

import de.codecentric.ochsen.core.model.*
import de.codecentric.ochsen.core.parser.Parser
import java.nio.file.Paths

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val parser = Parser(Paths.get("."))
        val api = parser.parse("foo.yaml")
        println(api)
    }
}