package com.example.finure.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response model for symbol search API.
 * Contains a list of best-matching tickers based on a keyword.
 */
data class SearchResultResponse(
    @SerializedName("bestMatches") val bestMatches: List<SymbolMatch>?
)

/**
 * Represents an individual search match result from the AlphaVantage symbol search.
 */
data class SymbolMatch(
    @SerializedName("1. symbol") val symbol: String,
    @SerializedName("2. name") val name: String,
    @SerializedName("4. region") val region: String,
    @SerializedName("8. currency") val currency: String
)
