package com.opends.processor.creators

import androidx.compose.ui.unit.Dp
import com.open.design.system.OpenDesignSystem
import com.open.design.system.Spacing
import com.opends.processor.PACKAGE
import com.opends.processor.openSpaceClass
import com.opends.processor.writeThemeAccessor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec

private const val INSTANCE_CLASS_NAME = "OpenSpaceInstance"

class SpaceCreator(
    private val themePropertyCreator: ThemePropertyCreator,
    private val filesTypesFactory: FilesTypesFactory,
) : TypeCreator {
    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.spacing.values.toSet()))

            add(writeSpacingInstance(content.spacing.values.toSet()))
        }
    }

    private fun createColorPallet(colors: Set<Spacing>): FileSpec {
        val lightColors = colors.map {
            it.meta.name to it.value
        }

        val mappedColors = lightColors.map {
            colorsToPropertySpec(
                it
            )
        }

        return FileSpec.builder(PACKAGE, "SpacePallet")
            .addProperties(mappedColors)
            .build()
    }

    private fun colorsToPropertySpec(
        pair: Pair<String, Float>
    ): PropertySpec {
        val member = MemberName("androidx.compose.ui.unit", "dp")

        return PropertySpec.builder(pair.first, Dp::class.java)
            .initializer("%L.%M", pair.second, member)
            .build()
    }

    private fun writeSpacingInstance(
        colors: Set<Spacing>
    ): FileSpec {
        val codeBlock = CodeBlock.builder()

        codeBlock.addStatement("OpenSpace(")

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name},")
        }

        codeBlock.addStatement(")")

        val property = PropertySpec.builder(INSTANCE_CLASS_NAME, openSpaceClass)
            .initializer(codeBlock.build())
            .build()

        return FileSpec.builder(PACKAGE, INSTANCE_CLASS_NAME)
            .addProperty(property)
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName("androidx.compose.ui.unit", "Dp")

        return writeThemeAccessor(
            "OpenSpace",
            content.spacing.values.toSet(),
            className
        ).toFileSpec()
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            creatorFilesName = filesTypesFactory
        )
    }
}
