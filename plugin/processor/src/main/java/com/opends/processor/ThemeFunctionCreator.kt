package com.opends.processor

import androidx.compose.runtime.Composable
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.UNIT

class ThemeFunctionCreator {

    private fun FileSpec.Builder.addImports(): FileSpec.Builder {
        return addImport(
            "androidx.compose.runtime",
            "getValue",
            "setValue",
            "mutableStateOf",
            "staticCompositionLocalOf",
            "CompositionLocalProvider",
        )
            .addImport("com.opends.typography", "typographyToMaterial3Typography")
            .addImport("com.opends.color", "colorToMaterial3Colors")
            .addImport("com.opends.color", "DarkOpenColorInstance")
            .addImport("com.opends.color", "LightOpenColorInstance")
            .addImport("androidx.compose.foundation", "isSystemInDarkTheme")
            .addImport("androidx.compose.material3", "MaterialTheme")
            .addImport("androidx.appcompat.app.", "AppCompatDelegate")
    }

    fun writeFile(
        fileName: String,
        type: TypeSpec,
    ) =
        FileSpec
            .builder(PACKAGE, fileName)
            .addImports()
            .addType(type)
            .addFunction(createIsNightFunction())
            .addFunction(
                FunSpec.builder("OpenDesignSystemTheme")
                    .addAnnotation(Composable::class.java)
                    .also {
                        val isDarkTheme =
                            MemberName(PACKAGE, "isNightMode")

                        it.addParameter(
                            ParameterSpec.builder("isDarkTheme", Boolean::class.java)
                                .defaultValue(
                                    CodeBlock.of("%M()", isDarkTheme)
                                )
                                .build()
                        )
                    }
                    .also {
                        val customAnnotation =
                            AnnotationSpec.builder(Composable::class.java).build()
                        it.addParameter(
                            ParameterSpec.builder(
                                "content",
                                LambdaTypeName.get(returnType = UNIT)
                                    .copy(annotations = listOf(customAnnotation))
                            )
                                .build()
                        )
                    }
                    .addCode(
                        createThemeCodeBlock()
                    )
                    .build()
            )
            .build()

    private fun createThemeCodeBlock(): CodeBlock {
        return CodeBlock.builder()
            .add(
                """
val colors = if (isDarkTheme) {
    DarkOpenColorInstance
} else {
    LightOpenColorInstance
}

val newColors = colorToMaterial3Colors(colors)
val newTypos = typographyToMaterial3Typography(typo = OpenDesignSystemTheme.typography)

CompositionLocalProvider(
        OpenDesignSystemTheme.LocalOpenDsColor provides colors,
        OpenDesignSystemTheme.LocalOpenDsTypography provides OpenDesignSystemTheme.typography,
        OpenDesignSystemTheme.LocalOpenDsSpace provides OpenDesignSystemTheme.space,
        OpenDesignSystemTheme.LocalOpenDsShadow provides OpenDesignSystemTheme.shadow
    ) {
MaterialTheme(
    colorScheme = newColors,
    typography = newTypos,
    content = content
)
}
                            """
            )
            .build()
    }

    private fun createIsNightFunction(): FunSpec {
        return FunSpec.builder("isNightMode")
            .addAnnotation(Composable::class.java)
            .returns(Boolean::class.java)
            .addCode(
                CodeBlock.builder()
                    .add(
"""
return when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}
"""
                    )
                    .build()
            )
            .build()
    }
}
