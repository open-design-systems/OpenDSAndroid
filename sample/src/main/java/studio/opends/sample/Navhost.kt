package studio.opends.sample

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opends.sample.screens.main.ColorScreen
import com.opends.sample.screens.main.HomeScreen
import com.opends.sample.screens.main.TypographyScreen

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavigationDestinations = NavigationDestinations.Start
) {
    var title by rememberSaveable { mutableStateOf("OpenDS") }
    var canPop by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(
                title = title,
                canPop = canPop,
                navController = navController,
            )
        }
    ) { padding ->
        NavHost(
            modifier = modifier.padding(padding),
            navController = navController,
            startDestination = startDestination.route
        ) {
            composable(
                route = NavigationDestinations.Start.route
            ) {
                canPop = false
                title = NavigationDestinations.Start.route
                HomeScreen(navController = navController)
            }
            composable(
                route = NavigationDestinations.ColorScreen.route
            ) {
                canPop = true
                title = NavigationDestinations.ColorScreen.route
                ColorScreen()
            }
            composable(
                route = NavigationDestinations.TypographyScreen.route
            ) {
                canPop = true
                title = NavigationDestinations.TypographyScreen.route
                TypographyScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    canPop: Boolean,
    navController: NavHostController
) {
    if (canPop) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            title = {
                Text(text = title)
            }
        )
    } else {
        TopAppBar(
            title = {
                Text(text = title)
            }
        )
    }
}
