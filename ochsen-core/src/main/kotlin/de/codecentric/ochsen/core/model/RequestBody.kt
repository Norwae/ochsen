package de.codecentric.ochsen.core.model

data class RequestBody(
        val content: Map<String, MediaTypeObject>,
        val required: Boolean
)
