package studio.opends.processor.creators

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import studio.opends.OpenDesignSystem
import studio.opends.processor.writeThemeAccessor
import studio.opends.Typography

class TypographyCreator(
    private val themePropertyCreator: ThemePropertyCreator,
    private val filesTypesFactory: FilesTypesFactory,
    private val typographyToMaterial: TypographyToMaterial
) : TypeCreator {
    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.typography.values.toSet()))

            add(writeSpacingInstance(content.typography.values.toSet()))
            add(typographyToMaterial.toMaterial3(content.typography.values.toSet()))
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

        return FileSpec.builder(
            filesTypesFactory.getPackage(),
            filesTypesFactory.getPalletFileName()
        )
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
    @Suppress("MagicNumber")
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
        else -> throw IllegalArgumentException("Invalid weight for font")
    }

    private fun colorsToPropertySpec(
        pair: Pair<String, Typography>
    ): PropertySpec {
        val member = MemberName("androidx.compose.ui.unit", "sp")
        val textStyle = ClassName("androidx.compose.ui.text", "TextStyle")
        val weight = MemberName("androidx.compose.ui.text.font", "FontWeight")

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

        val member = MemberName(filesTypesFactory.getPackage(), filesTypesFactory.openClass())
        codeBlock.addStatement("%M(", member)

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name},")
        }

        codeBlock.addStatement(")")

        val property =
            PropertySpec.builder(
                filesTypesFactory.createInstanceClassName(),
                filesTypesFactory.createClassName()
            )
                .initializer(codeBlock.build())
                .build()

        return FileSpec.builder(
            filesTypesFactory.getPackage(),
            filesTypesFactory.createInstanceClassName()
        )
            .addProperty(property)
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName("androidx.compose.ui.text", "TextStyle")

        return writeThemeAccessor(
            filesTypesFactory.openClass(),
            content.typography.values.toSet(),
            className
        ).toFileSpec(filesTypesFactory)
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            creatorFilesName = filesTypesFactory
        )
    }
}
