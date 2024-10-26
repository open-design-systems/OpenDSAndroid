package studio.opends.processor.creators

sealed interface CreatorFilesName {

    val baseName: String

    data class TypographyNames(
        override val baseName: String = "Typography",
    ) : CreatorFilesName

    data class ColorNames(
        override val baseName: String = "Color",
    ) : CreatorFilesName

    data class SpaceNames(
        override val baseName: String = "Space",
    ) : CreatorFilesName

    data class ShadowNames(
        override val baseName: String = "Shadow",
    ) : CreatorFilesName
}
