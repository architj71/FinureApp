package com.example.finure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.finure.ui.theme.FinureTheme
import com.example.finure.viewmodel.ThemeViewModel
import com.finure.app.navigation.MainNavigation
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point activity for the app. Hosts the composable content and sets up theming.
 * Uses Hilt for dependency injection and applies dynamic light/dark theme based on ThemeViewModel.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ThemeViewModel scoped to the activity, using Hilt
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            FinureTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Main app navigation composable
                    MainNavigation(themeViewModel)
                }
            }
        }
    }
}
