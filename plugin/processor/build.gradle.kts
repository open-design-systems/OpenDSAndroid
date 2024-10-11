plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    kotlin("plugin.serialization") version "2.0.0-RC3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.androidx.ui.graphics.desktop)
    implementation(libs.androidx.runtime.desktop)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":contract-models"))
}
