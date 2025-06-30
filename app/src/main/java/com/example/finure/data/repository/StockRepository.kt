package com.example.finure.data.repository

import android.util.Log
import com.example.finure.data.cache.CacheManager
import com.example.finure.data.model.CompanyOverview
import com.example.finure.data.model.SearchResultResponse
import com.example.finure.data.model.StockGainerResponse
import com.example.finure.data.network.AlphaVantageApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for stock-related API calls.
 * Implements in-memory caching to reduce redundant API usage.
 */
@Singleton
class StockRepository @Inject constructor(
    private val api: AlphaVantageApi
) {
    private val cacheExpiry = 10 * 60 * 1000L // 10 minutes

    /**
     * Fetches top gainers and losers.
     * Uses cache if recent data is available.
     */
    suspend fun getTopGainersAndLosers(): StockGainerResponse {
        val key = "top_movers"
        CacheManager.get<StockGainerResponse>(key, cacheExpiry)?.let {
            return it
        }
        val response = api.getTopGainersAndLosers()
        Log.d("StockRepository", "API Response: $response")
        CacheManager.put(key, response)
        return response
    }

    /**
     * Fetches company overview data by symbol.
     * Uses cache if recent.
     */
    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        val key = "overview_$symbol"
        CacheManager.get<CompanyOverview>(key, cacheExpiry)?.let {
            return it
        }
        val response = api.getCompanyOverview(symbol = symbol)
        CacheManager.put(key, response)
        return response
    }

    /**
     * Searches for a ticker based on keyword.
     * Uses cache if recent.
     */
    suspend fun searchTicker(keyword: String): SearchResultResponse {
        val key = "search_$keyword"
        CacheManager.get<SearchResultResponse>(key, cacheExpiry)?.let {
            return it
        }
        val response = api.searchTicker(keywords = keyword)
        CacheManager.put(key, response)
        return response
    }
}
