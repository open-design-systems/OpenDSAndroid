package com.opends.processor

import androidx.compose.runtime.Stable
import com.open.design.system.OpenType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName

private const val GET_TOKENS_FUNCTION = "getTokens"

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
        .addFunction(createFunctionToRetrieveTokens(content, type))
        .build()
}

private fun createFunctionToRetrieveTokens(
    content: Set<OpenType>,
    typeName: TypeName
): FunSpec {
    val codeBlock = CodeBlock.builder()

    codeBlock.add("return listOf(")
    for (item in content.map { it.meta.name }) {
        codeBlock.addStatement("%S to $item,", item)
    }
    codeBlock.add(")")
    codeBlock.build()

    val pairType = Pair::class.java.asClassName()
        .parameterizedBy(
            STRING,
            typeName
        )

    val returnType = LIST
        .parameterizedBy(pairType)

    val getTokens = FunSpec.builder(GET_TOKENS_FUNCTION)
        .addModifiers(KModifier.INTERNAL)
        .returns(returnType)
        .addCode(codeBlock.build())

    return getTokens
        .build()
}
