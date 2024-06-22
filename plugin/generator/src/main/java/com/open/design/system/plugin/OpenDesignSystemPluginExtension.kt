package com.open.design.system.plugin

import org.gradle.api.provider.Property

interface OpenDesignSystemPluginExtension {

    val themeLocation: Property<String>
}
