package com.finure.app.data.repository

import com.finure.app.data.api.AlphaVantageApi
import com.finure.app.data.model.StockGainerResponse
import javax.inject.Inject

class StockRepository @Inject constructor(
    private val api: AlphaVantageApi
) {
    private val apiKey = "YOUR_API_KEY" // Replace or inject later

    suspend fun fetchTopGainers(): StockGainerResponse {
        return api.getTopGainers(apiKey = apiKey)
    }
}
