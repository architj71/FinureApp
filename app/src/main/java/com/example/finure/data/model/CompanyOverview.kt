package com.finure.app.data.model

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
    val FiftyTwoWeekHigh: String? = null,
    val FiftyTwoWeekLow: String? = null
)
