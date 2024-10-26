package com.opends.sample.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.opends.OpenDesignSystemTheme

@Composable
fun ColorScreen(modifier: Modifier = Modifier) {
    val colorTokens = OpenDesignSystemTheme.color.getTokens()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding()
    ) {
        items(colorTokens) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 8.dp),
                    text = it.first.replaceFirstChar { it.uppercase() },
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(20.dp)
                        .background(it.second)
                        .border(width = 1.dp, color = Color.Black)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
private fun ColorScreenPreview() {
    OpenDesignSystemTheme {
        ColorScreen()
    }
}
