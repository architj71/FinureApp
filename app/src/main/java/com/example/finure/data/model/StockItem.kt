package com.example.finure.data.model

/**
 * Simplified representation of a stock item.
 * Used in UI for displaying top gainers/losers in grid format.
 */
data class StockItem(
    val symbol: String,
    val name: String,
    val price: String,
    val changePercent: String
)
