package com.finure.app.data.model

data class StockGainerResponse(
    val metadata: String?,
    val last_updated: String?,
    val top_gainers: List<StockInfo>?,
    val top_losers: List<StockInfo>?,
    val most_actively_traded: List<StockInfo>?
)

data class StockInfo(
    val ticker: String,
    val price: String,
    val change_amount: String,
    val change_percentage: String,
    val volume: String
)
