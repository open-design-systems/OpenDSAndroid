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

private const val LOCAL_COLORS = "LocalOpenDsColors"

val openColorsClass = ClassName(PACKAGE, "OpenColors")

fun processFile(
    input: String,
    output: File
): List<FileSpec> {
    Json {
        ignoreUnknownKeys = true
    }

    val decoded = Json.decodeFromString<OpenDesignSystemResponse>(input)

    val listOfFiles = mutableListOf<FileSpec>()
    val themeProperties = mutableSetOf<PropertySpec>()

    val creators = setOf(
        ColorCreator()
    )

    creators.forEach {
        listOfFiles.addAll(
            it.createFiles(decoded.defaultValues)
        )

        themeProperties.addAll(it.createThemeProperty())
    }

    val themeObject = writeThemeObject(themeProperties)

    writeTheme(themeObject, output)

    return listOfFiles
}

private fun writeThemeObject(
    properties: Set<PropertySpec>
) = TypeSpec.objectBuilder("OpenDesignSystemTheme")
    .addProperties(properties)
    .build()

private fun writeTheme(
    themeObject: TypeSpec,
    output: File
) {
    writeFile("OpenDesignSystemTheme", themeObject, output)
}

private fun writeFile(
    fileName: String,
    type: TypeSpec,
    output: File
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
        .writeTo(output)

@Throws(URISyntaxException::class, IOException::class)
fun readFileFromResources(filename: String): String {
    val resource: URL? = OpenDesignSystem::class.java.classLoader.getResource(filename)
    val bytes = Files.readAllBytes(Paths.get(resource!!.toURI()))
    return String(bytes)
}