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

    private val _selectedWatchlist = MutableStateFlow<String?>(null)
    val selectedWatchlist: StateFlow<String?> = _selectedWatchlist

    val watchlistsFlow: StateFlow<Map<String, List<StockInfo>>> = watchlistRepo.watchlists

    val watchlistNames: StateFlow<List<String>> = watchlistsFlow
        .map { it.keys.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val watchlist: StateFlow<List<StockInfo>> = combine(
        watchlistsFlow,
        _selectedWatchlist
    ) { watchlists, selectedName ->
        selectedName?.let { watchlists[it].orEmpty() } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            watchlistsFlow.collect { all ->
                if (_selectedWatchlist.value == null && all.isNotEmpty()) {
                    _selectedWatchlist.value = all.keys.first()
                }
            }
        }
    }

    fun selectWatchlist(name: String) {
        _selectedWatchlist.value = name
    }

    fun loadWatchlistNames() {
        // No-op, auto-updated via watchlistsFlow
    }

    fun loadWatchlist() {
        _selectedWatchlist.value?.let { selectWatchlist(it) }
    }
}
