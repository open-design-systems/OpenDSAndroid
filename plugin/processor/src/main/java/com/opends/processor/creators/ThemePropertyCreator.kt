package com.opends.processor.creators

import androidx.compose.runtime.Composable
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import java.util.Locale

class ThemePropertyCreator(
    private val staticComposition: StaticCompositionCreator,

    ) {

    fun createTheme(
        namePrefix: String = "",
        creatorFilesName: FilesTypesFactory
    ): Set<PropertySpec> {
        return buildSet {
            val colorProperty = PropertySpec.builder(
                creatorFilesName.baseName().lowercase(Locale.ENGLISH),
                creatorFilesName.createClassName()
            )
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(Composable::class.java)
                        .addStatement("return ${creatorFilesName.createStaticCompositionName()}.current")
                        .build()
                )
                .build()

            add(colorProperty)
            add(
                staticComposition.create(
                    creatorFilesName.createStaticCompositionName(),
                    namePrefix + creatorFilesName.createInstanceClassName(),
                    creatorFilesName.createClassName()
                )
            )
        }
    }
}
