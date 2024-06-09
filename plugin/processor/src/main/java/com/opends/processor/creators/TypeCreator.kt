package com.opends.processor.creators

import com.open.design.system.OpenDesignSystem
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec

interface TypeCreator {

    fun createFiles(content: OpenDesignSystem): Set<FileSpec>

    fun createThemeProperty() : Set<PropertySpec>
}