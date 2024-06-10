package com.opends.processor.creators

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.open.design.system.OpenDesignSystem
import com.open.design.system.Shadows
import com.opends.processor.PACKAGE
import com.opends.processor.writeThemeAccessor
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import java.lang.reflect.Type

class ShadowCreator(
    private val themePropertyCreator: ThemePropertyCreator,
    private val filesTypesFactory: FilesTypesFactory,
) : TypeCreator {
    override fun createFiles(content: OpenDesignSystem): Set<FileSpec> {
        return buildSet {
            add(
                createFileAccessors(content)
            )

            add(createColorPallet(content.shadows))

            add(createShadowType())

            add(writeSpacingInstance(content.shadows))
        }
    }

    class DataClassSpecBuilder(name: String) {

        private val typeSpec: TypeSpec.Builder = TypeSpec.classBuilder(name)
            .addModifiers(KModifier.DATA)
        private val constructor = FunSpec.constructorBuilder()


        fun build() = typeSpec
            .primaryConstructor(constructor.build())
            .build()

        fun addProperty(
            name: String,
            type: Type,
            vararg modifiers: KModifier
        ): DataClassSpecBuilder {
            val property = PropertySpec.builder(name, type, *modifiers)
                .initializer(name)
                .build()

            typeSpec.addProperty(property)
            constructor.addParameter(name, type)

            return this
        }

        fun addProperty(
            name: String,
            type: TypeName,
            vararg modifiers: KModifier,
        ): DataClassSpecBuilder {
            val property = PropertySpec.builder(name, type, *modifiers)
                .initializer(name)
                .build()

            typeSpec.addProperty(property)
            constructor.addParameter(name, type)

            return this
        }

        fun addAnnotation(annotation: Class<*>): DataClassSpecBuilder {
            typeSpec.addAnnotation(annotation)
            return this
        }
    }

    private fun createShadowType(): FileSpec {
        val shadowOffsetType = DataClassSpecBuilder("ShadowOffset")
            .addProperty("width", Dp::class.java)
            .addProperty("height", Dp::class.java)
            .addAnnotation(Immutable::class.java)
            .build()

        val shadowType = DataClassSpecBuilder("ShadowType")
            .addProperty("elevation", Dp::class.java)
            .addProperty("shadowColor", Color::class.java)
            .addProperty("opacity", Float::class.java)
            .addProperty("radius", Float::class.java)
            .addProperty("offset", ClassName(PACKAGE, "ShadowOffset"))
            .addAnnotation(Immutable::class.java)
            .build()

        return FileSpec.builder(PACKAGE, "ShadowType")
            .addTypes(listOf(shadowOffsetType, shadowType))
            .build()
    }

    private fun createColorPallet(colors: Set<Shadows>): FileSpec {
        val lightColors = colors.map {
            it.meta.name to it
        }

        val mappedColors = lightColors.map {
            colorsToPropertySpec(
                it
            )
        }

        return FileSpec.builder(PACKAGE, filesTypesFactory.getPalletFileName())
            .addProperties(mappedColors)
            .build()
    }

    private fun colorsToPropertySpec(
        pair: Pair<String, Shadows>
    ): PropertySpec {
        val memberColor = MemberName("androidx.compose.ui.graphics", "Color")
        val shadowTypeClass = ClassName(PACKAGE, "ShadowType")
        val member = MemberName(PACKAGE, "ShadowType")
        val dpMember = MemberName("androidx.compose.ui.unit", "dp")

        val offSetInstance = CodeBlock.builder()
            .add("ShadowOffset(")
            .add("width=%L.%M,", pair.second.shadowOffset.width, dpMember)
            .add("height=%L.%M,", pair.second.shadowOffset.height, dpMember)
            .add(")")
            .build()

        return PropertySpec.builder(pair.first, shadowTypeClass)
            .initializer(
                CodeBlock.builder()
                    .addStatement("%M(", member)
                    .addStatement("elevation=%L.%M,", pair.second.elevation, dpMember)
                    .addStatement("offset=%L,", offSetInstance)
                    .addStatement("opacity=%Lf,", pair.second.shadowOpacity)
                    .addStatement("radius=%Lf,", pair.second.shadowRadius)
                    .addStatement(
                        "shadowColor=%M(0xff%L)",
                        memberColor,
                        pair.second.shadowColor.removePrefix("#")
                    )
                    .addStatement(")")
                    .build()
            )
            .build()
    }

    private fun writeSpacingInstance(
        colors: Set<Shadows>
    ): FileSpec {
        val codeBlock = CodeBlock.builder()

        codeBlock.addStatement("${filesTypesFactory.openClass()}(")

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

        return FileSpec.builder(PACKAGE, filesTypesFactory.createInstanceClassName())
            .addProperty(property)
            .build()
    }

    private fun createFileAccessors(
        content: OpenDesignSystem
    ): FileSpec {
        val className = ClassName(PACKAGE, "ShadowType")

        return writeThemeAccessor(
            "OpenShadow",
            content.shadows,
            className
        ).toFileSpec()
    }

    override fun createThemeProperty(): Set<PropertySpec> {
        return themePropertyCreator.createTheme(
            creatorFilesName = filesTypesFactory
        )
    }
}