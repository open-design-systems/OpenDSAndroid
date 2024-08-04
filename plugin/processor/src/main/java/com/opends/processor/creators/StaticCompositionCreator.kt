package com.opends.processor.creators

import androidx.compose.runtime.ProvidableCompositionLocal
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asClassName

class StaticCompositionCreator {

    fun create(
        propertyName: String,
        instanceName: MemberName,
        classType: ClassName
    ): PropertySpec {
        return PropertySpec.Companion.builder(
            propertyName,
            ProvidableCompositionLocal::class.java.asClassName()
                .parameterizedBy(classType)
        )
            .initializer("staticCompositionLocalOf { %M }", instanceName)
            .build()
    }
}
