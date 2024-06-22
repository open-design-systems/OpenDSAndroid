package com.open.design.system.plugin

import com.android.build.gradle.AppExtension
import com.opends.processor.processFile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Files

class OpenDesignSystemPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create<OpenDesignSystemPluginExtension>(EXTENSION_NAME)

        target.afterEvaluate {
            val loadedFileLocation = extension.themeLocation

            with(extensions.getByType<AppExtension>()) {
                sourceSets {
                    this["debug"].java {
                        this.srcDirs("build/generated/source/debug")
                    }
                }
            }

            processFile(
                input = readFileFromResources(loadedFileLocation.get()),
                output = getGeneratedFolder(project)
            )

            target.tasks.create("createOpenDS") {
                processFile(
                    input = readFileFromResources(loadedFileLocation.get()),
                    output = getGeneratedFolder(project)
                )
            }
        }
    }

    /**
     * TODO: Temporary solution for fetch build dir
     */
    private fun getGeneratedFolder(project: Project): File {
        return project.layout.buildDirectory.file("generated/source/debug").get().asFile
    }

    @Throws(URISyntaxException::class, IOException::class)
    fun readFileFromResources(filename: String): String {
        val file = File(filename)
        val bytes = Files.readAllBytes(file.toPath())
        return String(bytes)
    }

    internal companion object {
        private const val EXTENSION_NAME = "openDS"
    }
}
