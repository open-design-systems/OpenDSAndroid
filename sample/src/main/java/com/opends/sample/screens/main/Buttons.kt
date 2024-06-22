package com.opends.sample.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opends.sample.theme.ProjectPreview
import com.opends.sample.theme.SampleTheme

@Composable
fun Button(
    modifier: Modifier = Modifier,
    buttons: List<String> = listOf("Colors", "Spacing", "Typography", "Shadows")
) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(horizontal = 70.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            for (nameButton in buttons) {
                Buttons(
                    nameButton = nameButton,
                    onContinuedClicked = {}
                )
            }
        }
    }
}

@Composable
fun Buttons(
    modifier: Modifier = Modifier,
    nameButton: String,
    onContinuedClicked: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        shape = ShapeDefaults.Large,
        onClick = onContinuedClicked
    ) {
        Text(text = nameButton)
    }
}

@ProjectPreview
@Composable
fun ButtonPreview() {
    SampleTheme {
        Button()
    }
}