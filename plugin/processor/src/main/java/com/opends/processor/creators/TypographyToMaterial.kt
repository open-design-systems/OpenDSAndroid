package com.opends.processor.creators

import androidx.compose.runtime.Composable
import com.open.design.system.Typography
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

class TypographyToMaterial constructor(
    private val filesTypesFactory: FilesTypesFactory
) {

    private val materialTypographyList = setOf(
        "displayLarge",
        "displayMedium",
        "displaySmall",
        "headlineLarge",
        "headlineMedium",
        "headlineSmall",
        "titleLarge",
        "titleMedium",
        "titleSmall",
        "bodyLarge",
        "bodyMedium",
        "bodySmall",
        "labelLarge",
        "labelMedium",
        "labelSmall",
    )

    fun toMaterial3(typo: Set<Typography>): FileSpec {
        val builder = CodeBlock.builder()
            .add("return androidx.compose.material3.MaterialTheme.typography.copy(")

        typo.filter { materialTypographyList.contains(it.meta.name) }
            .forEach {
                val propertyName = it.meta.name
                builder.addStatement("%L = typo.%L,", propertyName, propertyName)
            }

        builder.add(")")

        val funSpec = FunSpec.builder("typographyToMaterial3Typography")
            .addParameter("typo", filesTypesFactory.createClassName())
            .addAnnotation(Composable::class)
            .returns(ClassName("androidx.compose.material3", "Typography"))
            .addCode(builder.build())

        val file = FileSpec.builder(filesTypesFactory.getPackage(), "TypographyToMaterialTypography")
            .addFunction(funSpec.build())

        return file.build()
    }
}
