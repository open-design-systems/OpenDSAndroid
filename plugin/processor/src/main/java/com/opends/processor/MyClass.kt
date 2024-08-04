package com.opends.processor

import androidx.compose.runtime.Composable
import com.open.design.system.OpenDesignSystem
import com.open.design.system.RefType
import com.opends.processor.creators.ColorCreator
import com.opends.processor.creators.CreatorFilesName
import com.opends.processor.creators.FilesTypesFactory
import com.opends.processor.creators.ShadowCreator
import com.opends.processor.creators.SpaceCreator
import com.opends.processor.creators.StaticCompositionCreator
import com.opends.processor.creators.ThemePropertyCreator
import com.opends.processor.creators.TypographyCreator
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

typealias TokenMap = MutableMap<RefType, MutableMap<String, String>>

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

    val themePropertyCreator = ThemePropertyCreator(
        staticComposition = StaticCompositionCreator()
    )

    val tokensMap: TokenMap = mutableMapOf()

    val creators = setOf(
        ColorCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.ColorNames()),
            tokensMap = tokensMap
        ),
        SpaceCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.SpaceNames()),
        ),
        TypographyCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.TypographyNames()),
        ),
        ShadowCreator(
            themePropertyCreator = themePropertyCreator,
            filesTypesFactory = FilesTypesFactory(filesName = CreatorFilesName.ShadowNames()),
            tokensMap = tokensMap
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
        .addFunction(
            FunSpec.builder("OpenDesignSystemTheme")
                .addAnnotation(Composable::class.java)
                .also {
                    val isDarkTheme =
                        MemberName("androidx.compose.foundation", "isSystemInDarkTheme")

                    it.addParameter(
                        ParameterSpec.builder("isDarkTheme", Boolean::class.java)
                            .defaultValue(
                                CodeBlock.of("%M()", isDarkTheme)
                            )
                            .build()
                    )
                }
                .also {
                    val customAnnotation = AnnotationSpec.builder(Composable::class.java).build()
                    it.addParameter(
                        ParameterSpec.builder(
                            "content",
                            LambdaTypeName.get(returnType = UNIT)
                                .copy(annotations = listOf(customAnnotation))
                        )
                            .build()
                    )
                }
                .build()
        )
        .build()

@Throws(URISyntaxException::class, IOException::class)
fun readFileFromResources(filename: String): String {
    val resource: URL? = OpenDesignSystem::class.java.classLoader.getResource(filename)
    val bytes = Files.readAllBytes(Paths.get(resource!!.toURI()))
    return String(bytes)
}
