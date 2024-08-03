package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Typography(
    override val meta: MetaInformation,
    val fontFamily: String,
    val fontSize: Int,
    val fontWeight: Int,
    val lineHeight: Int,
    val letterSpacing: Float
) : OpenType
