plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

version = properties["POM_VERSION"].toString()

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

gradlePlugin {
    website.set("https://github.com/open-design-systems/OpenDSAndroid")
    vcsUrl.set("https://github.com/open-design-systems/OpenDSAndroid")

    plugins {
        register("opends") {
            id = "com.opends"
            displayName = "OpenDSAndroid"
            description = "OpenDSAndroid plugin to create android theme configuration"
            implementationClass = "com.open.design.system.plugin.OpenDesignSystemPlugin"
            tags.addAll(listOf("android", "theme", "compose-theme", "apps", "mobile"))
        }
    }
}

dependencies {
    implementation(gradleApi())

    implementation(project(":processor"))

    implementation(libs.plgn.gradle)
}

apply {
    from("$rootDir/publish.gradle")
}
