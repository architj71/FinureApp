package com.example.finure.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finure.data.model.StockInfo
import com.example.finure.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel to manage user watchlists.
 * Handles watchlist selection, loading state, and real-time updates.
 */
@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepo: WatchlistRepository
) : ViewModel() {

    private val _selectedWatchlist = MutableStateFlow<String?>(null)
    val selectedWatchlist: StateFlow<String?> = _selectedWatchlist

    // All watchlists stored in memory: Map<WatchlistName, List<Stocks>>
    val watchlistsFlow: StateFlow<Map<String, List<StockInfo>>> = watchlistRepo.watchlists


    // List of available watchlist names for selection UI
    val watchlistNames: StateFlow<List<String>> = watchlistsFlow
        .map { it.keys.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Current selected watchlist's stocks
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
        // Automatically select first available watchlist if none selected
        viewModelScope.launch {
            watchlistsFlow.collect { all ->
                if (_selectedWatchlist.value == null && all.isNotEmpty()) {
                    _selectedWatchlist.value = all.keys.first()
                }
            }
        }
    }

    /**
     * Update the selected watchlist.
     * This triggers recomposition of stock lists via [watchlist] flow.
     */
    fun selectWatchlist(name: String) {
        _selectedWatchlist.value = name
    }

    /**
     * Triggers selection refresh for UI, useful when watchlists update externally.
     */
    fun loadWatchlist() {
        _selectedWatchlist.value?.let { selectWatchlist(it) }
    }

    /**
     * Placeholder for consistency with other ViewModels.
     * Watchlist names update automatically via flow.
     */
    fun loadWatchlistNames() = Unit
}
