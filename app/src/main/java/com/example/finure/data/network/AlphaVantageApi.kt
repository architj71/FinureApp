package com.example.finure.data.network

import com.example.finure.data.model.CompanyOverview
import com.example.finure.data.model.SearchResultResponse
import com.example.finure.data.model.StockGainerResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface defining API endpoints for AlphaVantage stock data.
 * Includes APIs for top movers, company overview, and symbol search.
 */
interface AlphaVantageApi {

    /**
     * Fetches the top gainers and losers in the market.
     * This is a custom function expected to return grouped data.
     */
    @GET("query")
    suspend fun getTopGainersAndLosers(
        @Query("function") function: String = "TOP_GAINERS_LOSERS",
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8"
    ): StockGainerResponse

    /**
     * Retrieves detailed overview information for a given company symbol.
     */
    @GET("query")
    suspend fun getCompanyOverview(
        @Query("function") function: String = "OVERVIEW",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8"
    ): CompanyOverview

    /**
     * Searches for ticker symbols based on a keyword.
     * Useful for implementing a ticker autocomplete/search feature.
     */
    @GET("query")
    suspend fun searchTicker(
        @Query("function") function: String = "SYMBOL_SEARCH",
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = "YLG1ZHXQ507QQEU8"
    ): SearchResultResponse
}
