package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class Spacing(
    override val meta: MetaInformation,
    val value: Float
) : OpenType