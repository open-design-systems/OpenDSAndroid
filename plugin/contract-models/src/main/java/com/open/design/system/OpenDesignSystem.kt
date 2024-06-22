package com.open.design.system

import kotlinx.serialization.Serializable

@Serializable
data class OpenDesignSystem(
    val id: String,
    val meta: MetaInformation,
    val colors: Set<OpenColor>,
    val spacing: Set<Spacing>,
    val surface: Set<Surface>,
    val typography: Set<Typography>,
    val shadows: Set<Shadows>
)

@Serializable
data class OpenDesignSystemResponse(
    val defaultValues: OpenDesignSystem
)
