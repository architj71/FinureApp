package com.finure.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finure.app.data.model.CompanyOverview
import com.finure.app.data.model.StockInfo
import com.finure.app.data.repository.StockRepository
import com.finure.app.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: StockRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _stockOverview = MutableStateFlow<CompanyOverview?>(null)
    val stockOverview: StateFlow<CompanyOverview?> = _stockOverview

    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist: StateFlow<Boolean> = _isInWatchlist

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var currentStockInfo: StockInfo? = null

    fun loadOverview(symbol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.getCompanyOverview(symbol)
                _stockOverview.value = response

                currentStockInfo = StockInfo(
                    ticker = response?.Symbol ?: "",
                    price = response?.EPS ?: "0.0",
                    change_amount = "0.0",        // not available in overview API
                    change_percentage = "0.0",    // same here
                    volume = "0"                  // same here
                )

                _isInWatchlist.value = currentStockInfo?.let {
                    watchlistRepository.isInAnyWatchlist(it)
                } ?: false

            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleWatchlist(watchlistName: String) {
        currentStockInfo?.let { stock ->
            if (_isInWatchlist.value) {
                watchlistRepository.removeStockFromWatchlist(watchlistName, stock)
            } else {
                watchlistRepository.addStockToWatchlist(watchlistName, stock)
            }
            _isInWatchlist.value = !_isInWatchlist.value
        }
    }
}
