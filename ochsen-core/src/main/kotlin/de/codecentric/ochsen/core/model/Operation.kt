package de.codecentric.ochsen.core.model

data class Operation(
        val parameters: List<Parameter>?,
        val requestBody: RequestBody?,
        val responses: Map<String, Schema>?
)