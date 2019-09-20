package de.codecentric.ochsen.core.model

data class PathItem (
    val get: Operation?,
    val put: Operation?,
    val post: Operation?,
    val delete: Operation?,
    val options: Operation?,
    val head: Operation?,
    val patch: Operation?,
    val trace: Operation?
)
