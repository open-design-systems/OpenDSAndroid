package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Surface(
    val meta: MetaInformation,
    val borderColor: String,
    val borderRadius: Float,
    val borderWidth: Float,
    val boxShadow: String,
    val backgroundColor: String
)
