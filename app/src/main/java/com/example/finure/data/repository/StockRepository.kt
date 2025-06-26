package com.finure.app.data.repository

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

    suspend fun getTopGainersAndLosers(): StockGainerResponse {
        return api.getTopGainersAndLosers()
    }

    suspend fun getCompanyOverview(symbol: String): CompanyOverview {
        return api.getCompanyOverview(symbol = symbol)
    }

    suspend fun searchTicker(keyword: String): SearchResultResponse {
        return api.searchTicker(keywords = keyword)
    }
}
