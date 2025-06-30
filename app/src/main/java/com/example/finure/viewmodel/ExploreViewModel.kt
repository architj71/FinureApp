package com.example.finure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finure.data.model.StockItem
import com.example.finure.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Explore screen.
 * Currently loads and exposes only top gainers.
 */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _topGainers = MutableStateFlow<List<StockItem>>(emptyList())
    val topGainers: StateFlow<List<StockItem>> = _topGainers

    init {
        fetchGainers()
    }

    /**
     * Loads top gainers data on initialization.
     * Handles basic fallback on failure (empty list).
     */
    private fun fetchGainers() {
        viewModelScope.launch {
            try {
                val response = repository.getTopGainersAndLosers()
                _topGainers.value = (response.top_gainers ?: emptyList()) as List<StockItem>
            } catch (e: Exception) {
                _topGainers.value = emptyList() // Fallback: silent fail
            }
        }
    }
}
