package com.finure.app.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.finure.viewmodel.ThemeViewModel
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.WbSunny

/**
 * Entry composable that sets up the Scaffold with bottom navigation and theme toggle.
 * Embeds the navigation graph inside the scaffold content.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainNavigation(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomNavBar(navController) }
        ) {
            NavigationGraph(navController)
        }

        // Show theme toggle only on selected routes
        if (currentRoute == Screen.Stocks.route || currentRoute == Screen.Watchlist.route) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp, top = 23.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(
                    onClick = { themeViewModel.toggleTheme() },
                    modifier = Modifier.size(40.dp)
                ) {
                    val isDark = themeViewModel.isDarkTheme.collectAsState().value
                    Icon(
                        imageVector = if (isDark) Icons.Outlined.WbSunny else Icons.Outlined.Brightness4,
                        contentDescription = "Toggle Theme",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

/**
 * Bottom navigation bar showing key app destinations (Stocks, Watchlist).
 * Uses NavController to switch between composable destinations.
 */
@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(Screen.Stocks, Screen.Watchlist)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
