# OpenDSAndroid

![OpenDS logo](docs/images/opends-logo.png)

The official Open Design System Android Plugin provides the easiest way to have access to your Design System tokens into Android.

# Install

`OpenDSAndroid` is available trough jitpack, so we need to add it to the project:

1. Open you `settings.gradle` or `settings.gradle.kts`
2. Make sure that the repositories section contains jitpack

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url "https://www.jitpack.io" }
    }
}
```

3. Go to your desired module `build.gradle` and import `OpenDSAndroid`

```kotlin
dependencies {
    implementation("open-design-systems:OpenDSAndroid:<VERSION>")
}
```

# Creating a contract theme

You can create a contract theme in the [theme generator](https://open-design-systems.github.io/), there
 you will need to export the theme to later be used in your project

# Basic Usage

Once you have the plugin you can force the tokens to be create running

```
./gradlew sample:createOpenDS
``` 

or simple running your project (is important to remember that the module that you applied this 
library is being used by the project).

## Configuration

You have some configurations that can be done in the plugin

``` groovy
openDS {
    themeLocation = "$projectDir/theme/open-design-system.json"
}
```

| Config | Description                                                                                                                             |
|---|-----------------------------------------------------------------------------------------------------------------------------------------|
| themeLocation | point to the location where the theme is located in our project, in the given example is inside the folder theme in the `sample` module |

## Generated tokens

After everything done you will have some classes generated under `com.opends` path

You can access them trough the theme instance provided OpenDesignSystemTheme

```kotin
OpenDesignSystemTheme.colors.primary
OpenDesignSystemTheme.shadows.level1
```

Also you have a theme composable that you can use that already handle night/day variations and 
convert the colors to material3 support

```kotlin
OpenDesignSystemTheme {
    // your composable screen here
}
```

## Material support

The theme generate already convert your tokens to material3 tokens. to do that your token just need to have
the same name as the material token

This is done for colors and Typography

## Recommendations

We recommend to use OpenDSAndroid in just one module (that would be your design system module) and
expose this module, this way only one instance of token will be created

# R8 / Proguard

TODO

# License