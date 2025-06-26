package com.finure.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finure.app.data.model.StockItem
import com.finure.app.model.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WatchlistState(
    val isLoading: Boolean = false,
    val stocks: List<StockItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class WatchlistViewModel @Inject constructor() : ViewModel() {

    private val _watchlistState = MutableStateFlow(WatchlistState(isLoading = true))
    val watchlistState: StateFlow<WatchlistState> = _watchlistState

    init {
        loadWatchlist()
    }

    private fun loadWatchlist() {
        viewModelScope.launch {
            try {
                // Simulated delay; replace with actual DataStore/Room fetch
                delay(1000)
                val dummy = listOf(
                    StockItem("AAPL", "Apple Inc.", "192.54"),
                    StockItem("GOOGL", "Alphabet Inc.", "2798.65")
                )
                _watchlistState.value = WatchlistState(stocks = dummy)
            } catch (e: Exception) {
                _watchlistState.value = WatchlistState(error = e.message)
            }
        }
    }
}
