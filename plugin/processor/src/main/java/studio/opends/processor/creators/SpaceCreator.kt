package studio.opends.processor.creators

import androidx.compose.ui.unit.Dp
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import studio.opends.OpenDesignSystem
import studio.opends.Spacing
import studio.opends.processor.writeThemeAccessor

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

        return FileSpec.builder(filesTypesFactory.getPackage(), "SpacePallet")
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

        val member = MemberName(filesTypesFactory.getPackage(), filesTypesFactory.openClass())
        codeBlock.addStatement("%M(", member)

        colors.forEach {
            codeBlock.addStatement("${it.meta.name}=${it.meta.name},")
        }

        codeBlock.addStatement(")")

        val property = PropertySpec.builder(
            filesTypesFactory.createInstanceClassName(),
            filesTypesFactory.createClassName()
        )
            .initializer(codeBlock.build())
            .build()

        return FileSpec.builder(filesTypesFactory.getPackage(), filesTypesFactory.createInstanceClassName())
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
        ).toFileSpec(filesTypesFactory)
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            creatorFilesName = filesTypesFactory
        )
    }
}
