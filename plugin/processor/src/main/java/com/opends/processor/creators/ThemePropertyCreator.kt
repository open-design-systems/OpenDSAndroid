package com.opends.processor.creators

import androidx.compose.runtime.Composable
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
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
            val instanceMemberName = MemberName(
                creatorFilesName.getPackage(),
                namePrefix + creatorFilesName.createInstanceClassName()
            )
            add(
                staticComposition.create(
                    creatorFilesName.createStaticCompositionName(),
                    instanceMemberName,
                    creatorFilesName.createClassName()
                )
            )
        }
    }
}
