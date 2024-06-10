package com.opends.processor

import androidx.compose.runtime.Stable
import com.open.design.system.OpenType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

private const val GET_TOKENS_FUNCTION = "gettokens"

fun writeThemeAccessor(
    name: String,
    content: Set<OpenType>,
    type: ClassName
): TypeSpec {
    val mapped = content.map {
        PropertySpec.builder(
            it.meta.name,
            type
        )
            .mutable()
            .delegate("mutableStateOf(${it.meta.name})")
            .setter(
                FunSpec.setterBuilder()
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .build()
    }

    val mappedParameters = content.map {
        ParameterSpec.builder(
            it.meta.name,
            type
        ).build()
    }



    return TypeSpec.classBuilder(name)
        .addProperties(mapped)
        .addAnnotation(Stable::class)
        .primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameters(mappedParameters)
                .build()
        )
        .addFunction(createFunctionToRetrieveTokens(content))
        .build()
}

private fun createFunctionToRetrieveTokens(
    content: Set<OpenType>,
): FunSpec {
    val getTokens = FunSpec.builder(GET_TOKENS_FUNCTION)
        .addModifiers(KModifier.INTERNAL)
        .addStatement("listOf(")

    for (item in content.map { it.meta.name }) {

        getTokens.addStatement("%S to $item,", item)
    }

    return getTokens.addStatement(")")
        .build()
}
