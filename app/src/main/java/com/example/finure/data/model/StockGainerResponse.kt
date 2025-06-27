package com.finure.app.data.model

data class StockGainerResponse(
    val metadata: String? = null,
    val last_updated: String? = null,
    val top_gainers: List<StockInfo> = emptyList(),
    val top_losers: List<StockInfo> = emptyList(),
    val most_actively_traded: List<StockInfo> = emptyList()
)


data class StockInfo(
    val ticker: String,
    val price: String,
    val change_amount: String,
    val change_percentage: String,
    val volume: String
)
