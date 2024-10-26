package studio.opends.processor.creators

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import studio.opends.OpenDesignSystem

interface TypeCreator {

    fun createFiles(content: OpenDesignSystem): Set<FileSpec>

    fun createThemeProperty(): Set<PropertySpec>
}
