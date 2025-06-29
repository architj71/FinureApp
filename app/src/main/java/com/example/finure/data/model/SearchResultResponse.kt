package com.finure.app.data.model

import com.google.gson.annotations.SerializedName

data class SearchResultResponse (
    @SerializedName("bestMatches") val bestMatches: List<SymbolMatch>?
)

data class SymbolMatch(
    @SerializedName("1. symbol") val symbol: String,
    @SerializedName("2. name") val name: String,
    @SerializedName("4. region") val region: String,
    @SerializedName("8. currency") val currency: String
)
