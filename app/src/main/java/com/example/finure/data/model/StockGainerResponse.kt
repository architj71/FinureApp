package com.finure.app.data.model

data class StockGainerResponse(
    val topGainers: List<StockItem> = emptyList(),
    val topLosers: List<StockItem> = emptyList()
)
