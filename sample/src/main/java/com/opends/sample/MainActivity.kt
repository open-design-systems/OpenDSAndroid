package com.opends.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.open.design.system.OpenDesignSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenDesignSystemTheme {
                Button()
            }
        }
    }
}

@Composable
fun Button(
    modifier: Modifier = Modifier,
    buttons: List<String> = listOf("Colors", "Spacing", "Typography", "Shadows")
) {
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

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun ButtonPreview() {
    Button()
}
