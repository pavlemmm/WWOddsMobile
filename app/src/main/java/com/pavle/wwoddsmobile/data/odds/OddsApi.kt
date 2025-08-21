package com.pavle.wwoddsmobile.data.odds

import retrofit2.http.GET

interface OddsApi {
    @GET("odds")
    suspend fun getOdds(): Map<String, List<EventDto>>
}
