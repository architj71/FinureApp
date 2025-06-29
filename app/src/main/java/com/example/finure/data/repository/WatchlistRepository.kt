package com.finure.app.data.repository

import com.finure.app.data.model.StockInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor() {

    private val _watchlists = MutableStateFlow<Map<String, MutableList<StockInfo>>>(emptyMap())

    val watchlists: StateFlow<Map<String, List<StockInfo>>>
        get() = _watchlists.map { it.mapValues { entry -> entry.value.toList() } }
            .stateIn(
                scope = kotlinx.coroutines.GlobalScope, // Replace with proper scope if needed
                started = SharingStarted.Eagerly,
                initialValue = emptyMap()
            )

    fun getAllWatchlistNames(): List<String> {
        return _watchlists.value.keys.toList()
    }

    fun getStocksInWatchlist(name: String): List<StockInfo> {
        return _watchlists.value[name]?.toList() ?: emptyList()
    }

    fun addStockToWatchlist(name: String, stock: StockInfo) {
        val updated = _watchlists.value.toMutableMap()
        val list = updated.getOrPut(name) { mutableListOf() }
        if (stock !in list) {
            list.add(stock)
            _watchlists.value = updated
        }
    }

    fun removeStockFromWatchlist(name: String, stock: StockInfo) {
        val updated = _watchlists.value.toMutableMap()
        updated[name]?.remove(stock)
        _watchlists.value = updated
    }

    fun clearAll() {
        _watchlists.value = emptyMap()
    }

    // Optional utility
    fun createEmptyWatchlist(name: String) {
        val updated = _watchlists.value.toMutableMap()
        updated.getOrPut(name) { mutableListOf() }
        _watchlists.value = updated
    }
}
