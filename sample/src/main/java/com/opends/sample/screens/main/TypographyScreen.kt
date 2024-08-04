package com.opends.sample.screens.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opends.OpenDesignSystemTheme

@Composable
fun TypographyScreen(modifier: Modifier = Modifier) {
    val typographyTokens = OpenDesignSystemTheme.typography.getTokens()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding()
    ) {
        items(typographyTokens) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    style = it.second,
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = it.first.replaceFirstChar { it.uppercase() },
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun TypographyScreenPreview() {
    OpenDesignSystemTheme {
        TypographyScreen()
    }
}