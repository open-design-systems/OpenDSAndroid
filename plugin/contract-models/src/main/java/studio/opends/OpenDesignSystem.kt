package studio.opends

import kotlinx.serialization.Serializable

@Serializable
data class OpenDesignSystem(
    val meta: MetaInformation,
    val colors: Map<String, OpenColor>,
    val spacing: Map<String, Spacing>,
    // val surface: Map<String, Surface>,
    val typography: Map<String, Typography>,
    val shadows: Map<String, Shadows>
)
