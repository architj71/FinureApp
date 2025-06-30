package com.example.finure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finure.data.model.StockGainerResponse
import com.example.finure.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to manage top movers (gainers/losers) data for the dashboard screen.
 * Retrieves stock data via [StockRepository].
 */
@HiltViewModel
class StockViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _gainerData = MutableStateFlow<StockGainerResponse?>(null)
    val gainerData: StateFlow<StockGainerResponse?> = _gainerData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Loads top gainers and losers data and updates corresponding state.
     * Handles loading and error indicators for UI.
     */
    fun loadTopMovers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getTopGainersAndLosers()
                _gainerData.value = response
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
