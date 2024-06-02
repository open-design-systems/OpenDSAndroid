plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

gradlePlugin {
    plugins {
        register("opends") {
            id = "com.opends"
            implementationClass = "com.open.design.system.plugin.OpenDesignSystemPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation(project(":processor"))

    implementation(libs.plgn.gradle)
}
