package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Shadows(
    override val meta: MetaInformation,
    val elevation: Float,
    val shadowColor: String,
    val shadowOpacity: Float,
    val shadowOffset: ShadowOffset,
    val shadowRadius: Float
) : OpenType

@Serializable
data class ShadowOffset(
    val width: Float,
    val height: Float
)
