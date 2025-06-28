package com.finure.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finure.ui.screens.ViewAllScreen
import com.example.finure.ui.screens.WatchlistDetailScreen
import com.finure.app.ui.screens.*

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Stocks.route) {
        composable(Screen.Stocks.route) {
            StocksScreen(navController)
        }
        composable(Screen.Watchlist.route) {
            WatchlistListScreen(navController)
        }
        composable("watchlist_detail/{name}") {
            val name = it.arguments?.getString("name") ?: ""
            WatchlistDetailScreen(navController, name)
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
