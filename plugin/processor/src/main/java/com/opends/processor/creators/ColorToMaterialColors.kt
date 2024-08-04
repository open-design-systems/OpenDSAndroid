package com.opends.processor.creators

import androidx.compose.runtime.Composable
import com.open.design.system.OpenColor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

class ColorToMaterialColors constructor(
    private val filesTypesFactory: FilesTypesFactory
) {

    private val materialColorsList = setOf(
        "primary",
        "onPrimary",
        "primaryContainer",
        "onPrimaryContainer",
        "inversePrimary",
        "secondary",
        "onSecondary",
        "secondaryContainer",
        "onSecondaryContainer",
        "tertiary",
        "onTertiary",
        "tertiaryContainer",
        "onTertiaryContainer",
        "background",
        "onBackground",
        "surface",
        "onSurface",
        "surfaceVariant",
        "onSurfaceVariant",
        "surfaceTint",
        "inverseSurface",
        "inverseOnSurface",
        "error",
        "onError",
        "errorContainer",
        "onErrorContainer",
        "outline",
        "outlineVariant",
        "scrim",
        "surfaceBright",
        "surfaceDim",
        "surfaceContainer",
        "surfaceContainerHigh",
        "surfaceContainerHighest",
        "surfaceContainerLow",
        "surfaceContainerLowest",
    )

    fun toMaterial3Colors(colors: Set<OpenColor>): FileSpec {
        val builder = CodeBlock.builder()
            .add("return androidx.compose.material3.MaterialTheme.colorScheme.copy(")

        colors.filter { materialColorsList.contains(it.meta.name) }
            .forEach {
                val propertyName = it.meta.name
                builder.addStatement("%L = colors.%L,", propertyName, propertyName)
            }

        builder.add(")")

        val funSpec = FunSpec.builder("colorToMaterial3Colors")
            .addParameter("colors", filesTypesFactory.createClassName())
            .addAnnotation(Composable::class)
            .returns(ClassName("androidx.compose.material3", "ColorScheme"))
            .addCode(builder.build())

        val file = FileSpec.builder(filesTypesFactory.getPackage(), "ColorToMaterialConverter")
            .addFunction(funSpec.build())

        return file.build()
    }
}
