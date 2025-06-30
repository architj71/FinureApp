package com.example.finure.data.model

import com.google.gson.annotations.SerializedName

/**
 * Detailed financial and descriptive information about a company.
 * Retrieved from the AlphaVantage "OVERVIEW" endpoint.
 */
data class CompanyOverview(
    val Symbol: String?,
    val Name: String?,
    val Description: String?,
    val Sector: String?,
    val Industry: String?,
    val MarketCapitalization: String?,
    val PERatio: String?,
    val EPS: String?,
    val DividendPerShare: String?,
    val DividendYield: String?,
    val AnalystTargetPrice: String?,
    val ReturnOnEquityTTM: String?,
    val ProfitMargin: String?,
    val Currency: String?,
    val Country: String?,
    val Exchange: String?,
    val OfficialSite: String?,

    @SerializedName("52WeekHigh")
    val fiftyTwoWeekHigh: String?,

    @SerializedName("52WeekLow")
    val fiftyTwoWeekLow: String?
)

