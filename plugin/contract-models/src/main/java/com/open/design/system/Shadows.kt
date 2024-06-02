package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Shadows(
    val meta: MetaInformation,
    val elevation: String,
    val shadowColor: String,
    val shadowOpacity: Float,
    val shadowOffset: ShadowOffset,
    val shadowRadius: Float
)

@Serializable
data class ShadowOffset(
    val width: Float,
    val height: Float
)
