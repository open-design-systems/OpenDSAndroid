package studio.opends

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefInformation(
    @SerialName("\$ref") val ref: String,
    @SerialName("\$refType") val type: String,
)
