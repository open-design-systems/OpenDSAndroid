package com.opends.processor.creators

import androidx.compose.runtime.Composable
import com.opends.processor.openTypographyClass
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec

class ThemePropertyCreator(
    private val staticComposition: StaticCompositionCreator,

    ) {

    fun createTheme(
        creatorFilesName: FilesTypesFactory
    ): Set<PropertySpec> {
        return buildSet {
            val colorProperty = PropertySpec.builder(
                creatorFilesName.baseName(),
                creatorFilesName.createClassName()
            )
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(Composable::class.java)
                        .addStatement("return ${creatorFilesName.createStatlicCompositionName()}.current")
                        .build()
                )
                .build()

            add(colorProperty)
            add(
                staticComposition.create(
                    creatorFilesName.createStatlicCompositionName(),
                    creatorFilesName.createInstanceClassName(),
                    openTypographyClass
                )
            )
        }
    }
}