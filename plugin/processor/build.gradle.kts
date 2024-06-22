plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    kotlin("plugin.serialization") version "2.0.0-RC3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("androidx.compose.ui:ui-graphics-desktop:1.6.7")
    implementation("androidx.compose.runtime:runtime-desktop:1.6.7")
    implementation("com.squareup:kotlinpoet:1.17.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0-RC")
    implementation(project(":contract-models"))
}
