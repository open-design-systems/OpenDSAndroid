package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class OpenColor(
    override val meta: MetaInformation,
    val light: ColorData,
    val dark: ColorData
): OpenType

@Serializable
data class ColorData(
    val hex: String,
    val rgba: RawColor
)

@Serializable
data class RawColor(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int
)
