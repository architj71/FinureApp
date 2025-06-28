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

    private val _watchlistNames = MutableStateFlow<List<String>>(emptyList())
    val watchlistNames: StateFlow<List<String>> = _watchlistNames

    private val _selectedWatchlist = MutableStateFlow<String?>(null)
    val selectedWatchlist: StateFlow<String?> = _selectedWatchlist

    private val _watchlist = MutableStateFlow<List<StockInfo>>(emptyList())
    val watchlist: StateFlow<List<StockInfo>> = _watchlist

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadWatchlistNames() {
        viewModelScope.launch {
            val names = watchlistRepo.getAllWatchlistNames()
            _watchlistNames.value = names

            // ✅ Only call first() when list is not empty
            if (_selectedWatchlist.value == null && names.isNotEmpty()) {
                selectWatchlist(names.first())
            }
        }
    }


    fun selectWatchlist(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedWatchlist.value = name
            _error.value = null
            try {
                _watchlist.value = watchlistRepo.getStocksInWatchlist(name)
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadWatchlist() {
        _selectedWatchlist.value?.let { selectWatchlist(it) }
    }
}
