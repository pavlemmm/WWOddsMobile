package com.pavle.wwoddsmobile.data.odds

import com.google.gson.annotations.SerializedName

data class EventDto(
    val id: String,
    @SerializedName("sport_key")   val sportKey: String,
    @SerializedName("sport_title") val sportTitle: String,
    @SerializedName("commence_time") val commenceTime: String,
    @SerializedName("home_team")   val homeTeam: String,
    @SerializedName("away_team")   val awayTeam: String,
    val bookmakers: List<BookmakerDto>? = null
)

data class BookmakerDto(
    val key: String,
    val title: String,
    @SerializedName("last_update") val lastUpdate: String?,
    val markets: List<MarketDto>? = null
)

data class MarketDto(
    val key: String,
    @SerializedName("last_update") val lastUpdate: String?,
    val outcomes: List<OutcomeDto>? = null
)

data class OutcomeDto(
    val name: String,
    val price: Double
)
