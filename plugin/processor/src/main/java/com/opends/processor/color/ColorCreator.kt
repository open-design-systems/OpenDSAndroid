package com.opends.processor.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.graphics.Color
import com.open.design.system.ColorData
import com.open.design.system.OpenColor
import com.open.design.system.OpenDesignSystem
import com.opends.processor.PACKAGE
import com.opends.processor.TypeCreator
import com.opends.processor.openColorsClass
import com.opends.processor.writeThemeAccessor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

private const val LOCAL_COLORS = "LocalOpenDsColors"

class ColorCreator : TypeCreator {

    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.colors))

            add(writeColorInstance("Light", content.colors))
            add(writeColorInstance("Dark", content.colors))
        }
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return buildSet {
            val colorProperty = PropertySpec
                .builder("color", openColorsClass)
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(Composable::class.java)
                        .addStatement("return $LOCAL_COLORS.current")
                        .build()
                )
                .build()

            add(colorProperty)
            add(createLocalColorStaticComposition())
        }
    }

    private fun createLocalColorStaticComposition(): PropertySpec {
        return PropertySpec.builder(
            LOCAL_COLORS,
            ProvidableCompositionLocal::class.java.asClassName().parameterizedBy(openColorsClass)
        )
            .addModifiers(KModifier.PRIVATE)
            .initializer("staticCompositionLocalOf { LightOpenDSColors }")
            .build()
    }

    fun writeColorInstance(
        modifier: String,
        colors: Set<OpenColor>
    ): FileSpec {
        val codeBlock = CodeBlock.builder()

        codeBlock.addStatement("OpenColors(")

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name}${modifier},")
        }

        codeBlock.addStatement(")")

        val property = PropertySpec.builder("${modifier}OpenDSColors", openColorsClass)
            .initializer(codeBlock.build())
            .build()

        return FileSpec.builder(PACKAGE, "${modifier}OpenDSColors")
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
                "Light",
                it
            )
        }

        val mappedColorsDark = darkColors.map {
            colorsToPropertySpec(
                "Dark",
                it
            )
        }

        return FileSpec.builder(PACKAGE, "ColorPallet")
            .addProperties(mappedColors + mappedColorsDark)
            .build()
    }

    private fun colorsToPropertySpec(
        type: String,
        pair: Pair<String, ColorData>
    ): PropertySpec {
        val alpha = pair.second.rgba.alpha.toString(16)

        return PropertySpec.builder("${pair.first}$type", Color::class.java)
            .initializer("Color(0x$alpha${pair.second.hex.removePrefix("#")})")
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName("androidx.compose.ui.graphics", "Color")

        return writeThemeAccessor(
            "OpenColors",
            content.colors,
            className
        ).toFileSpec()
    }
}

fun TypeSpec.toFileSpec(): FileSpec {
    return FileSpec
        .builder(PACKAGE, this.name!!)
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