package de.codecentric.ochsen.core.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Parameter(
        val name: String,
        @JsonProperty("in")
        val location: ParameterLocation,
        val required: Boolean,
        val allowEmpty: Boolean
)