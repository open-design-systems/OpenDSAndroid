package studio.opends.sample

private enum class Destinations {
    START,
    ColorScreen,
    TypographySrceen
}

sealed interface NavigationDestinations {

    val route: String

    data object Start : NavigationDestinations {
        override val route: String = Destinations.START.name
    }
    data object ColorScreen : NavigationDestinations {
        override val route: String = Destinations.ColorScreen.name
    }
    data object TypographyScreen : NavigationDestinations {
        override val route: String = Destinations.TypographySrceen.name
    }
}
