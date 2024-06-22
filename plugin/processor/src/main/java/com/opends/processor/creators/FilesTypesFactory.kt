package com.opends.processor.creators

import com.opends.processor.PACKAGE
import com.squareup.kotlinpoet.ClassName

class FilesTypesFactory(
    private val filesName: CreatorFilesName
) {

    private val typeName = filesName.baseName

    fun getPalletFileName() = "${typeName}Pallet"

    fun createInstanceClassName() = "Open${typeName}Instance"

    fun createStaticCompositionName() = "LocalOpenDs$typeName"

    fun createClassName(): ClassName = ClassName(PACKAGE, openClass())
    fun baseName() = typeName
    fun openClass(): String = "Open$typeName"
}
