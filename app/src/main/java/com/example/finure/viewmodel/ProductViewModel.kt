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
    private val stockRepository: StockRepository,
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {

    private val _stockOverview = MutableStateFlow<CompanyOverview?>(null)
    val stockOverview: StateFlow<CompanyOverview?> = _stockOverview

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _watchlistNames = MutableStateFlow<List<String>>(emptyList())
    val watchlistNames: StateFlow<List<String>> = _watchlistNames

    private val _selectedWatchlists = MutableStateFlow<Set<String>>(emptySet())
    val selectedWatchlists: StateFlow<Set<String>> = _selectedWatchlists

    private var currentStock: StockInfo? = null

    fun loadOverview(symbol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = stockRepository.getCompanyOverview(symbol)
                _stockOverview.value = response

                currentStock = StockInfo(
                    ticker = symbol,
                    price = response.MarketCapitalization ?: "0",
                    change_amount = "",
                    change_percentage = "",
                    volume = ""
                )

                _watchlistNames.value = watchlistRepository.getAllWatchlistNames()
                updateSelectedWatchlists()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createWatchlist(name: String) {
        if (name.isNotBlank() && name !in _watchlistNames.value) {
            watchlistRepository.createEmptyWatchlist(name) // Initializes list
            _watchlistNames.value = watchlistRepository.getAllWatchlistNames()
            updateSelectedWatchlists() // ⬅ to refresh checkbox state
        }
    }


    fun toggleWatchlist(name: String, selected: Boolean) {
        currentStock?.let { stock ->
            if (selected) {
                watchlistRepository.addStockToWatchlist(name, stock)
            } else {
                watchlistRepository.removeStockFromWatchlist(name, stock)
            }
            updateSelectedWatchlists()
        }
    }

    private fun updateSelectedWatchlists() {
        currentStock?.let { stock ->
            val allNames = watchlistRepository.getAllWatchlistNames()
            _watchlistNames.value = allNames

            val selected = allNames
                .filter { name -> stock in watchlistRepository.getStocksInWatchlist(name) }
                .toSet()
            _selectedWatchlists.value = selected
        }
    }

}
