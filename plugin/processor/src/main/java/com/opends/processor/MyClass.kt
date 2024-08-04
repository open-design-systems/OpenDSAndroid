package com.opends.processor

import com.open.design.system.OpenDesignSystem
import com.opends.processor.creators.ColorCreator
import com.opends.processor.creators.ColorNightCreator
import com.opends.processor.creators.ColorToMaterialColors
import com.opends.processor.creators.CreatorFilesName
import com.opends.processor.creators.FilesTypesFactory
import com.opends.processor.creators.ShadowCreator
import com.opends.processor.creators.SpaceCreator
import com.opends.processor.creators.StaticCompositionCreator
import com.opends.processor.creators.ThemePropertyCreator
import com.opends.processor.creators.TypographyCreator
import com.opends.processor.creators.TypographyToMaterial
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

const val PACKAGE = "com.opends"

fun processFile(
    input: String,
    output: File
) {
    val jsonBuilder = Json {
        ignoreUnknownKeys = true
    }

    val decoded = jsonBuilder.decodeFromString<OpenDesignSystem>(input)

    val listOfFiles = mutableListOf<FileSpec>()
    val themeProperties = mutableSetOf<PropertySpec>()
    val colorNightCreator = ColorNightCreator()

    val themePropertyCreator = ThemePropertyCreator(
        staticComposition = StaticCompositionCreator()
    )

    val colorsFilesName = FilesTypesFactory(filesName = CreatorFilesName.ColorNames())
    val typographyFilesName = FilesTypesFactory(filesName = CreatorFilesName.TypographyNames())

    val creators = setOf(
        ColorCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = colorsFilesName,
            colorNightCreator = colorNightCreator,
            colorToMaterialColors = ColorToMaterialColors(filesTypesFactory = colorsFilesName)
        ),
        SpaceCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.SpaceNames()),
        ),
        TypographyCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = typographyFilesName,
            typographyToMaterial = TypographyToMaterial(typographyFilesName)
        ),
        ShadowCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.ShadowNames()),
            colorNightCreator = colorNightCreator,
        )
    )

    creators.forEach {
        listOfFiles.addAll(
            it.createFiles(decoded)
        )

        themeProperties.addAll(it.createThemeProperty())
    }

    val themeObject = writeThemeObject(themeProperties)

    listOfFiles.add(writeTheme(themeObject))

    listOfFiles.forEach {
        it.writeTo(output)
    }
}

private fun writeThemeObject(
    properties: Set<PropertySpec>
) = TypeSpec.objectBuilder("OpenDesignSystemTheme")
    .addProperties(properties)
    .build()

private fun writeTheme(
    themeObject: TypeSpec,
) = ThemeFunctionCreator().writeFile("OpenDesignSystemTheme", themeObject)

@Throws(URISyntaxException::class, IOException::class)
fun readFileFromResources(filename: String): String {
    val resource: URL? = OpenDesignSystem::class.java.classLoader.getResource(filename)
    val bytes = Files.readAllBytes(Paths.get(resource!!.toURI()))
    return String(bytes)
}
