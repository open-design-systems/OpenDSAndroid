package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Typography(
    override val meta: MetaInformation,
    val fontFamily: String,
    val fontSize: String,
    val fontWeight: Int,
    val lineHeight: String,
    val letterSpacing: Float
) : OpenType
