package com.finure.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finure.app.data.model.StockInfo
import com.finure.app.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepo: WatchlistRepository
) : ViewModel() {

    private val _watchlist = MutableStateFlow<List<StockInfo>>(emptyList())
    val watchlist: StateFlow<List<StockInfo>> = _watchlist

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadWatchlist() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val data = watchlistRepo.getAllWatchlistStocks()
                _watchlist.value = data
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }
}
