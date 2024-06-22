package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class MetaInformation(
    val name: String,
    val description: String
)
