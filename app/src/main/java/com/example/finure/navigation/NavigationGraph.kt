package com.finure.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finure.ui.screens.ViewAllScreen
import com.example.finure.ui.screens.WatchlistDetailScreen
import com.finure.app.ui.screens.*
import com.finure.app.navigation.Screen

/**
 * Defines the navigation graph for the application using Jetpack Navigation Compose.
 * Maps route names to corresponding composable screens.
 */
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Stocks.route) {
        composable(Screen.Stocks.route) {
            StocksScreen(navController)
        }
        composable(Screen.Watchlist.route) {
            WatchlistListScreen(navController)
        }
        composable("watchlist_detail/{name}") { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            key(name) {
                WatchlistDetailScreen(navController, name)
            }
        }

        composable(Screen.Explore.route) {
            ExploreScreen(navController)
        }
        composable("${Screen.Product.route}/{symbol}") {
            val symbol = it.arguments?.getString("symbol") ?: ""
            ProductScreen(navController, symbol)
        }
        composable("${Screen.ViewAll.route}/{type}") {
            val type = it.arguments?.getString("type") ?: ""
            ViewAllScreen(navController, type)
        }
    }
}
