package com.opends.processor

import androidx.compose.runtime.Stable
import com.open.design.system.OpenType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

fun <T> writeThemeAccessor(
    name: String,
    colors: Set<OpenType>,
    type: Class<T>
): TypeSpec {
    val mapped = colors.map {
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

    val mappedParameters = colors.map {
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
        .build()
}