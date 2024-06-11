package com.opends.sample

private enum class Destinations {
    START,
    ColorScreen
}

sealed interface NavigationDestinations {

    val route: String

    data object Start : NavigationDestinations {
        override val route: String = Destinations.START.name
    }
    data object ColorScreen : NavigationDestinations {
        override val route: String = Destinations.ColorScreen.name
    }
}