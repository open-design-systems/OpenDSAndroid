package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Spacing(
    val meta : MetaInformation,
    val value: Float
)