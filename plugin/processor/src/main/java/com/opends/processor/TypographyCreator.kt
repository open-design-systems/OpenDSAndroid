package com.opends.processor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import com.open.design.system.OpenDesignSystem
import com.open.design.system.Typography
import com.opends.processor.color.toFileSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName

private const val LOCAL_TYPO = "LocalOpenDsTypography"
private const val INSTANCE_CLASS_NAME = "OpenTypographyInstance"

class TypographyCreator : TypeCreator {
    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.typography))

            add(writeSpacingInstance(content.typography))
        }
    }

    private fun createColorPallet(colors: Set<Typography>): FileSpec {
        val lightColors = colors.map {
            it.meta.name to it
        }

        val mappedColors = lightColors.map {
            colorsToPropertySpec(
                it
            )
        }

        return FileSpec.builder(PACKAGE, "TypographyPallet")
            .addProperties(mappedColors)
            .build()
    }

    /**
     * const fontWeights = [
     *     { label: "Thin", value: 100 },
     *     { label: "Extra Light", value: 200 },
     *     { label: "Light", value: 300 },
     *     { label: "Regular", value: 400 },
     *     { label: "Medium", value: 500 },
     *     { label: "Semi Bold", value: 600 },
     *     { label: "Bold", value: 700 },
     *     { label: "Extra Bold", value: 800 },
     *     { label: "Black", value: 900 },
     * ];
     */
    private fun Int.toWeight() = when (this) {
        100 -> "Thin"
        200 -> "ExtraLight"
        300 -> "Light"
        400 -> "Normal"
        500 -> "Medium"
        600 -> "SemiBold"
        700 -> "Bold"
        800 -> "ExtraBold"
        900 -> "Black"
        else -> throw Exception("Invalid weight for font")
    }

    private fun colorsToPropertySpec(
        pair: Pair<String, Typography>
    ): PropertySpec {
        val member = MemberName("androidx.compose.ui.unit", "sp")
        val textStyle = ClassName("androidx.compose.ui.text", "TextStyle")
        val weight = MemberName("androidx.compose.ui.text.font", "FontWeight")

        when (pair.second.fontWeight) {

        }

        return PropertySpec.builder(pair.first, textStyle)
            .initializer(
                CodeBlock.builder()
                    .addStatement("TextStyle(")
                    .addStatement("fontWeight = %M.%L,", weight, pair.second.fontWeight.toWeight())
                    .addStatement("fontSize = %L.%M,", pair.second.fontSize, member)
                    .addStatement("lineHeight = %L.%M,", pair.second.lineHeight, member)
                    .addStatement(")")
                    .build()
            )
            .build()
    }

    private fun writeSpacingInstance(
        colors: Set<Typography>
    ): FileSpec {
        val codeBlock = CodeBlock.builder()

        codeBlock.addStatement("OpenTypography(")

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name},")
        }

        codeBlock.addStatement(")")

        val property = PropertySpec.builder(INSTANCE_CLASS_NAME, openTypographyClass)
            .initializer(codeBlock.build())
            .build()

        return FileSpec.builder(PACKAGE, INSTANCE_CLASS_NAME)
            .addProperty(property)
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName("androidx.compose.ui.text", "TextStyle")

        return writeThemeAccessor(
            "OpenTypography",
            content.typography,
            className
        ).toFileSpec()
    }

    @OptIn(DelicateKotlinPoetApi::class)
    override fun createThemeProperty(): Set<PropertySpec> {
        return buildSet {
            val colorProperty = PropertySpec.builder("typography", openTypographyClass)
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(Composable::class.java)
                        .addStatement("return $LOCAL_TYPO.current")
                        .build()
                )
                .build()

            add(colorProperty)
            add(createLocalColorStaticComposition())
        }
    }

    private fun createLocalColorStaticComposition(): PropertySpec {
        return PropertySpec.Companion.builder(
            LOCAL_TYPO,
            ProvidableCompositionLocal::class.java.asClassName()
                .parameterizedBy(openTypographyClass)
        )
            .addModifiers(KModifier.PRIVATE)
            .initializer("staticCompositionLocalOf { $INSTANCE_CLASS_NAME }")
            .build()
    }
}