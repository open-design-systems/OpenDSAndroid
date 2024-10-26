package studio.opends

import kotlinx.serialization.Serializable

@Serializable
data class MetaInformation(
    val id: String,
    val name: String,
    val description: String
)
