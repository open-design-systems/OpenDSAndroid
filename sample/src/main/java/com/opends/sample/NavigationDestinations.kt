package com.opends.sample

private enum class Destinations {
    START
}

sealed interface NavigationDestinations {

    val route: String

    data object Start : NavigationDestinations {
        override val route: String = Destinations.START.name
    }
}
