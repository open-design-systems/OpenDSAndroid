package com.opends.processor

import com.open.design.system.OpenDesignSystem
import com.open.design.system.OpenDesignSystemResponse
import com.opends.processor.color.ColorCreator
import com.squareup.kotlinpoet.ClassName
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

const val PACKAGE = "com.open.design.system"

val openColorsClass = ClassName(PACKAGE, "OpenColors")
val openSpaceClass = ClassName(PACKAGE, "OpenSpace")

fun processFile(
    input: String,
    output: File
) {
    Json {
        ignoreUnknownKeys = true
    }

    val decoded = Json.decodeFromString<OpenDesignSystemResponse>(input)

    val listOfFiles = mutableListOf<FileSpec>()
    val themeProperties = mutableSetOf<PropertySpec>()

    val creators = setOf(
        ColorCreator(),
        SpaceCreator()
    )

    creators.forEach {
        listOfFiles.addAll(
            it.createFiles(decoded.defaultValues)
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
) = writeFile("OpenDesignSystemTheme", themeObject)

private fun writeFile(
    fileName: String,
    type: TypeSpec,
) =
    FileSpec
        .builder(PACKAGE, fileName)
        .addImport(
            "androidx.compose.runtime",
            "getValue",
            "setValue",
            "mutableStateOf",
            "staticCompositionLocalOf"
        )
        .addType(type)
        .build()

@Throws(URISyntaxException::class, IOException::class)
fun readFileFromResources(filename: String): String {
    val resource: URL? = OpenDesignSystem::class.java.classLoader.getResource(filename)
    val bytes = Files.readAllBytes(Paths.get(resource!!.toURI()))
    return String(bytes)
}