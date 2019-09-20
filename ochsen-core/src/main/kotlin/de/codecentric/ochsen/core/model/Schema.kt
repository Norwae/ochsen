package de.codecentric.ochsen.core.model

data class Schema(
    val type: SchemaObjectType?,
    val properties: Map<String, Schema>?,
    val required: List<String>?,
    val format: String?,
    val enum: List<String>?
)
