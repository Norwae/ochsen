package de.codecentric.ochsen.core.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAPI3Document(
        @JsonProperty("openapi")
        val openApiVersion: String,
        val paths: Map<String, PathItem>,
        val components: Components
)