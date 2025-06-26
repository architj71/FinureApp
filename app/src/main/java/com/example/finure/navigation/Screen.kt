package com.finure.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Stocks : Screen("stocks", "Stocks", Icons.Default.TrendingUp)
    object Watchlist : Screen("watchlist", "Watchlist", Icons.Default.Star)
    object Explore : Screen("explore", "Explore", Icons.Default.Explore)
    object Product : Screen("product", "Product", Icons.Default.Info)
    object ViewAll : Screen("view_all", "View All", Icons.Default.List)
}

