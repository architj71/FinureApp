package com.example.finure.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Global theme toggle ViewModel for light/dark mode switching.
 * Can be shared across screens via CompositionLocalProvider.
 */
class ThemeViewModel : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    /**
     * Toggle between light and dark themes.
     */
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}
