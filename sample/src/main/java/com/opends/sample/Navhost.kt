package com.opends.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.opends.sample.screens.main.ColorScreen
import com.opends.sample.screens.main.HomeScreen

@Composable
internal fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: NavigationDestinations = NavigationDestinations.Start
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(
            route = NavigationDestinations.Start.route
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = NavigationDestinations.ColorScreen.route
        ) {
           ColorScreen()
        }
    }
}
