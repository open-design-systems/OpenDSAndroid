package studio.opends.plugin

import org.gradle.api.provider.Property

interface OpenDesignSystemPluginExtension {

    val themeLocation: Property<String>
}
