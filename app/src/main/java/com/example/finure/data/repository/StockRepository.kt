package com.finure.app.data.repository

import com.finure.app.data.cache.CacheManager
import com.finure.app.data.model.CompanyOverview
import com.finure.app.data.model.SearchResultResponse
import com.finure.app.data.model.StockGainerResponse
import com.finure.app.data.network.AlphaVantageApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepository @Inject constructor(
    private val api: AlphaVantageApi
) {
    private val cacheExpiry = 5 * 60 * 1000L // 5 minutes

    suspend fun getTopGainersAndLosers(): StockGainerResponse {
        val key = "top_movers"
        CacheManager.get<StockGainerResponse>(key, cacheExpiry)?.let {
            return it
        }
        val response = api.getTopGainersAndLosers()
        CacheManager.put(key, response)
        return response
    }

    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        val key = "overview_$symbol"
        CacheManager.get<CompanyOverview>(key, cacheExpiry)?.let {
            return it
        }
        val response = api.getCompanyOverview(symbol = symbol)
        CacheManager.put(key, response)
        return response
    }

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