package com.opends.processor.creators

import androidx.compose.ui.graphics.Color
import com.open.design.system.ColorData
import com.open.design.system.OpenColor
import com.open.design.system.OpenDesignSystem
import com.opends.processor.writeThemeAccessor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

class ColorCreator(
    private val themePropertyCreator: ThemePropertyCreator,
    private val filesTypesFactory: FilesTypesFactory,
    private val colorNightCreator: ColorNightCreator
) : TypeCreator {

    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            val colors = content.colors.values.toSet()
            add(createColorPallet(colors))

            add(writeColorInstance(colorNightCreator.getLightColorModifierName(), colors))
            add(writeColorInstance(colorNightCreator.getDarkColorModifierName(), colors))
        }
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            namePrefix = COLOR_INSTANCE_MODIFIER_LIGHT,
            creatorFilesName = filesTypesFactory
        )
    }

    private fun writeColorInstance(
        modifier: String,
        colors: Set<OpenColor>
    ): FileSpec {
        val propertyName = modifier + filesTypesFactory.createInstanceClassName()

        val codeBlock = CodeBlock.builder()

        val member = MemberName(filesTypesFactory.getPackage(), filesTypesFactory.openClass())
        codeBlock.addStatement("%M(", member)

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name}$modifier,")
        }

        codeBlock.addStatement(")")

        val property = PropertySpec.builder(
            propertyName,
            filesTypesFactory.createClassName()
        )
            .initializer(codeBlock.build())
            .build()

        return FileSpec.builder(filesTypesFactory.getPackage(), propertyName)
            .addProperty(property)
            .build()
    }

    private fun createColorPallet(colors: Set<OpenColor>): FileSpec {
        val lightColors = colors.map {
            it.meta.name to it.light
        }

        val darkColors = colors.map {
            it.meta.name to it.dark
        }

        val mappedColors = lightColors.map {
            colorsToPropertySpec(
                COLOR_INSTANCE_MODIFIER_LIGHT,
                it
            )
        }

        val mappedColorsDark = darkColors.map {
            colorsToPropertySpec(
                COLOR_INSTANCE_MODIFIER_DARK,
                it
            )
        }

        return FileSpec.builder(
            filesTypesFactory.getPackage(),
            filesTypesFactory.getPalletFileName()
        )
            .addProperties(mappedColors + mappedColorsDark)
            .build()
    }

    private fun colorsToPropertySpec(
        type: String,
        pair: Pair<String, ColorData>
    ): PropertySpec {
        val alphaToColorRange = pair.second.rgba.alpha * MAX_INT_COLOR_RANGE_CONVERTER
        val alpha = alphaToColorRange.toString(STRING_HEX_RADIX)

        return PropertySpec.builder("${pair.first}$type", Color::class.java)
            .initializer("Color(0x$alpha${pair.second.hex.removePrefix("#")})")
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName("androidx.compose.ui.graphics", "Color")

        return writeThemeAccessor(
            filesTypesFactory.openClass(),
            content.colors.values.toSet(),
            className
        ).toFileSpec(filesTypesFactory)
    }

    private companion object {
        private const val COLOR_INSTANCE_MODIFIER_LIGHT = "Light"
        private const val COLOR_INSTANCE_MODIFIER_DARK = "Dark"
        private const val STRING_HEX_RADIX = 16
        private const val MAX_INT_COLOR_RANGE_CONVERTER = 255
    }
}

fun TypeSpec.toFileSpec(filesTypesFactory: FilesTypesFactory): FileSpec {
    return FileSpec
        .builder(filesTypesFactory.getPackage(), this.name!!)
        .addImport(
            "androidx.compose.runtime",
            "getValue",
            "setValue",
            "mutableStateOf",
            "staticCompositionLocalOf",
        )
        .addType(this)
        .build()
}
