package com.opends.sample.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.opends.color.DarkOpenColorInstance
import com.opends.color.LightOpenColorInstance
import com.opends.color.colorToMaterial3Colors
import com.opends.typography.OpenTypographyInstance

@Composable
fun SampleTheme(content: @Composable () -> Unit) {
    val colors = if (isNightMode()) {
        DarkOpenColorInstance
    } else {
        LightOpenColorInstance
    }

    val newColors = colorToMaterial3Colors(colors)

    val typography = MaterialTheme.typography.copy(
        bodyMedium = OpenTypographyInstance.bodyMedium,
        headlineMedium = OpenTypographyInstance.headlineMedium
    )

    MaterialTheme(
        colorScheme = newColors,
        typography = typography,
        content = content
    )
}

@Composable
fun isNightMode() = when (AppCompatDelegate.getDefaultNightMode()) {
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}
