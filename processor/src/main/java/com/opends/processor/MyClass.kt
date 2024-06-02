package com.opends.processor

import androidx.compose.ui.graphics.Color
import com.open.design.system.OpenDesignSystem
import com.open.design.system.OpenDesignSystemResponse
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import kotlinx.serialization.json.Json
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

const val PACKAGE = "com.open.design.system"

fun main() {

    val result = readFileFromResources("sample.json")

    Json {
        ignoreUnknownKeys = true
    }

    val decoded = Json.decodeFromString<OpenDesignSystemResponse>(result)

    val colorFile =
        writeThemeAccessor(
            "OpenColors",
            decoded.defaultValues.colors,
            Color::class.java
        )
    writeFile("OpenColor", colorFile)

    val set = buildSet {
        val color = PropertySpec
            .builder("color", ClassName(PACKAGE, "OpenColors"))
            .getter(
                FunSpec.getterBuilder()

                    .build()
            )
            .build()

        add(color)
    }

    val themeObject = writeThemeObject(set)

    writeTheme(themeObject)
}

private fun writeThemeObject(
    properties: Set<PropertySpec>
) = TypeSpec.objectBuilder("OpenDesignSystemTheme")
    .addProperties(properties)
    .build()

private fun writeTheme(
    themeObject: TypeSpec
) {
    writeFile("OpenDesignSystemTheme", themeObject)
}

private fun writeFile(
    fileName: String,
    type: TypeSpec
) =
    FileSpec
        .builder(PACKAGE, fileName)
        .addType(type)
        .build()
        .writeTo(System.out)

@Throws(URISyntaxException::class, IOException::class)
fun readFileFromResources(filename: String): String {
    val resource: URL? = OpenDesignSystem::class.java.classLoader.getResource(filename)
    val bytes = Files.readAllBytes(Paths.get(resource!!.toURI()))
    return String(bytes)
}