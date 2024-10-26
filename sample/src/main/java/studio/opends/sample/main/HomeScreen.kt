package com.opends.sample.screens.main

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.opends.OpenDesignSystemTheme
import studio.opends.sample.NavigationDestinations

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    buttons: List<Pair<String, NavigationDestinations>> = listOf(
        "Colors" to NavigationDestinations.ColorScreen,
        "Typography" to NavigationDestinations.TypographyScreen,
    )
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        buttons.forEach { (nameButton, destination) ->
            Buttons(
                nameButton = nameButton,
                onContinuedClicked = {
                    navController.navigate(destination.route)
                }
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
private fun HomeScreenPreview() {
    OpenDesignSystemTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}
