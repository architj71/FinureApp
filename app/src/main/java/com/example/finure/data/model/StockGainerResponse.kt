package com.example.finure.data.model

/**
 * Response model for top gainers, losers, and most actively traded stocks.
 * This object encapsulates various categories returned by the backend.
 */
data class StockGainerResponse(
    val metadata: String? = null,
    val last_updated: String? = null,
    val top_gainers: List<StockInfo> = emptyList(),
    val top_losers: List<StockInfo> = emptyList(),
    val most_actively_traded: List<StockInfo> = emptyList()
)

/**
 * Core stock details used across various features like lists, watchlists, etc.
 */
data class StockInfo(
    val ticker: String = "",
    val price: String = "",
    val change_amount: String = "",
    val change_percentage: String = "",
    val volume: String = ""
)

