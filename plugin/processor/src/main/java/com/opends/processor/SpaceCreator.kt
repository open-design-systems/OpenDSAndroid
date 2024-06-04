package com.opends.processor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.unit.Dp
import com.open.design.system.OpenDesignSystem
import com.open.design.system.Spacing
import com.opends.processor.color.toFileSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName

private const val LOCAL_SPACE = "LocalOpenDsSpace"
private const val INSTANCE_CLASS_NAME = "OpenSpaceInstance"

class SpaceCreator : TypeCreator {
    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.spacing))

            add(writeSpacingInstance(content.spacing))
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
        return writeThemeAccessor(
            "OpenSpace",
            content.spacing,
            Dp::class.java
        ).toFileSpec()
    }

    @OptIn(DelicateKotlinPoetApi::class)
    override fun createThemeProperty(): Set<PropertySpec> {
        return buildSet {
            val colorProperty = PropertySpec
                .builder("space", openSpaceClass)
                .getter(
                    FunSpec.getterBuilder()
                        .addAnnotation(Composable::class.java)
                        .addStatement("return ${LOCAL_SPACE}.current")
                        .build()
                )
                .build()

            add(colorProperty)
            add(createLocalColorStaticComposition())
        }
    }

    private fun createLocalColorStaticComposition(): PropertySpec {
        return PropertySpec.builder(
            LOCAL_SPACE,
            ProvidableCompositionLocal::class.java.asClassName().parameterizedBy(openSpaceClass)
        )
            .addModifiers(KModifier.PRIVATE)
            .initializer("staticCompositionLocalOf { $INSTANCE_CLASS_NAME }")
            .build()
    }
}