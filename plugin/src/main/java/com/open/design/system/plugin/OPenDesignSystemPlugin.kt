package com.open.design.system.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.extra

class OPenDesignSystemPlugin: Plugin<Project> {
    override fun apply(target: Project) {

        target.afterEvaluate {


        }
    }

    internal companion object {
        private const val EXTENSION_NAME = "region"
    }
}