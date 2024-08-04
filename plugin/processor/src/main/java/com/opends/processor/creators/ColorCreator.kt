package com.opends.processor.creators

import androidx.compose.ui.graphics.Color
import com.open.design.system.ColorData
import com.open.design.system.OpenColor
import com.open.design.system.OpenDesignSystem
import com.open.design.system.RefType
import com.opends.processor.TokenMap
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
    private val tokensMap: TokenMap,
) : TypeCreator {

    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            val colors = content.colors.values.toSet()
            add(createColorPallet(colors))

            add(writeColorInstance(COLOR_INSTANCE_MODIFIER_LIGHT, colors))
            add(writeColorInstance(COLOR_INSTANCE_MODIFIER_DARK, colors))
        }
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            namePrefix = COLOR_INSTANCE_MODIFIER_LIGHT,
            creatorFilesName = filesTypesFactory
        )
    }

    private fun setColorInColorRefMap(
        openColor: OpenColor,
        value: String,
        modifier: String
    ) {
        tokensMap.getOrPut(RefType.colors) {
            mutableMapOf()
        }[openColor.meta.name+modifier] = value
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
            setColorInColorRefMap(
                it,
                "${it.meta.name}$modifier",
                modifier
            )
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

        return FileSpec.builder(filesTypesFactory.getPackage(), filesTypesFactory.getPalletFileName())
            .addProperties(mappedColors + mappedColorsDark)
            .build()
    }

    private fun colorsToPropertySpec(
        type: String,
        pair: Pair<String, ColorData>
    ): PropertySpec {
        val alphaToColorRange = pair.second.rgba.alpha * 255
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
