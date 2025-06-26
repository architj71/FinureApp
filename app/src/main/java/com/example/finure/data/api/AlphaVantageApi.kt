package com.finure.app.data.api

import com.finure.app.data.model.StockGainerResponse
import com.finure.app.data.model.StockItem
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageApi {
    @GET("query")
    suspend fun getTopGainers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String
    ): StockGainerResponse // custom wrapper class

    // Add other endpoints here later (e.g., overview, search)
}
