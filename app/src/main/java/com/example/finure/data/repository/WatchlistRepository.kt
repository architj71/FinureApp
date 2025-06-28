package com.finure.app.data.repository

import com.finure.app.data.model.StockInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchlistRepository @Inject constructor() {

    // Simulated in-memory storage; replace with DataStore/Room later
    private val watchlists = mutableMapOf<String, MutableList<StockInfo>>()

    fun getAllWatchlistNames(): List<String> {
        return watchlists.keys.toList()
    }

    fun getStocksInWatchlist(name: String): List<StockInfo> {
        return watchlists[name] ?: emptyList()
    }

    fun addStockToWatchlist(name: String, stock: StockInfo) {
        val list = watchlists.getOrPut(name) { mutableListOf() }
        if (stock !in list) list.add(stock)
    }

    fun removeStockFromWatchlist(name: String, stock: StockInfo) {
        watchlists[name]?.remove(stock)
    }

    fun isInAnyWatchlist(stock: StockInfo): Boolean {
        return watchlists.values.any { it.contains(stock) }
    }

    fun getAllWatchlistStocks(): List<StockInfo> {
        return watchlists.values.flatten()
    }

    fun isStockInWatchlist(watchlist: String, stock: StockInfo): Boolean {
        return watchlists[watchlist]?.contains(stock) == true
    }

    fun getWatchlistsContainingStock(stock: StockInfo): List<String> {
        return watchlists.filter { it.value.contains(stock) }.map { it.key }
    }


    fun clearAll() {
        watchlists.clear()
    }
}
