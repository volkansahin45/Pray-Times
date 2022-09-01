package com.vsahin.praytimes.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.vsahin.praytimes.ui.about.AboutScreen
import com.vsahin.praytimes.ui.home.HomeScreen
import com.vsahin.praytimes.ui.locationSelector.LocationSelectorScreen

const val HOME = "home"
const val LOCATION_SELECTOR = "locationSelector"
const val ABOUT = "about"

@Composable
fun Navigation() {
    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = HOME,
    ) {
        composable(HOME) {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }
        composable(LOCATION_SELECTOR) {
            LocationSelectorScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable(ABOUT) {
            AboutScreen()
        }
    }
}