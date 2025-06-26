package com.finure.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.finure.app.ui.screens.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Stocks.route) {
        composable(Screen.Stocks.route) { StocksScreen(navController) }
        composable(Screen.Watchlist.route) { WatchlistScreen(navController) }
        composable(Screen.Explore.route) { ExploreScreen(navController) }
        composable("${Screen.Product.route}/{symbol}") {
            ProductScreen(navController, it.arguments?.getString("symbol") ?: "")
        }
        composable("${Screen.ViewAll.route}/{type}") {
            ViewAllScreen(navController, it.arguments?.getString("type") ?: "")
        }
    }
}
