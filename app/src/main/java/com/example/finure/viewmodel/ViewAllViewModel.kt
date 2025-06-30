package com.example.finure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finure.data.model.StockInfo
import com.example.finure.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for fetching and exposing top gainers/losers stock data.
 * Backed by [StockRepository] and used in `ViewAllScreen`.
 */
@HiltViewModel
class ViewAllViewModel @Inject constructor (
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _stocks = MutableStateFlow<List<StockInfo>>(emptyList())
    val stocks: StateFlow<List<StockInfo>> = _stocks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Loads either top gainers or top losers based on [type] ("gainers" or "losers").
     * Handles loading state and error reporting.
     */
    fun loadStocks(type: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = stockRepository.getTopGainersAndLosers()
                _stocks.value = when (type.lowercase()) {
                    "gainers" -> response.top_gainers ?: emptyList()
                    "losers" -> response.top_losers ?: emptyList()
                    else -> emptyList()
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
