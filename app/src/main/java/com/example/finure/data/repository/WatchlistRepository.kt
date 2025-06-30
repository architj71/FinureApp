package com.example.finure.data.repository

import com.example.finure.data.model.StockInfo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository to manage user-created watchlists stored in-memory.
 * Exposes watchlists as reactive [StateFlow].
 */
@Singleton
class WatchlistRepository @Inject constructor() {

    // Internal mutable map: watchlist name -> list of stocks
    private val _watchlists = MutableStateFlow<Map<String, MutableList<StockInfo>>>(emptyMap())

    /**
     * Public watchlists flow, exposed as immutable snapshot.
     * Internally converts mutable lists to immutable.
     */
    @OptIn(DelicateCoroutinesApi::class)
    val watchlists: StateFlow<Map<String, List<StockInfo>>> = _watchlists
        .map { it.mapValues { (_, v) -> v.toList() } }
        .stateIn(
            scope = GlobalScope, // Replace with structured scope (e.g. viewModelScope) if needed
            started = SharingStarted.Eagerly,
            initialValue = emptyMap()
        )

    /** Returns all watchlist names */
    fun getAllWatchlistNames(): List<String> = _watchlists.value.keys.toList()

    /** Returns all stocks in a specific watchlist */
    fun getStocksInWatchlist(name: String): List<StockInfo> =
        _watchlists.value[name]?.toList() ?: emptyList()

    /** Adds a stock to a named watchlist */
    fun addStockToWatchlist(name: String, stock: StockInfo) {
        val current = _watchlists.value
        val newMap = current.toMutableMap()
        val oldList = newMap[name] ?: mutableListOf()

        if (stock !in oldList) {
            newMap[name] = oldList.toMutableList().apply { add(stock) } // ✅ create new list
            _watchlists.value = newMap // ✅ new map triggers flow update
        }
    }

    /** Removes a stock from a named watchlist */
    fun removeStockFromWatchlist(name: String, stock: StockInfo) {
        val current = _watchlists.value
        val newMap = current.toMutableMap()
        val oldList = newMap[name] ?: return

        newMap[name] = oldList.toMutableList().apply { remove(stock) } // ✅ new list
        _watchlists.value = newMap // ✅ new map
    }


    /** Clears all watchlists */
    fun clearAll() {
        _watchlists.value = emptyMap()
    }

    /** Creates an empty watchlist if it doesn't exist */
    fun createEmptyWatchlist(name: String) {
        val updated = _watchlists.value.toMutableMap()
        updated.getOrPut(name) { mutableListOf() }
        _watchlists.value = updated
    }
}
