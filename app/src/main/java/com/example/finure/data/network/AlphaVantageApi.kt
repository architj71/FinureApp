package com.finure.app.data.network

import com.finure.app.data.model.CompanyOverview
import com.finure.app.data.model.SearchResultResponse
import com.finure.app.data.model.StockGainerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AlphaVantageApi {

    @GET("query")
    suspend fun getTopGainersAndLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8 "
    ): StockGainerResponse

    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8 "
    ): CompanyOverview

    @GET("query")
    suspend fun searchTicker(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8 "
    ): SearchResultResponse
}
