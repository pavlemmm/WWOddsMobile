package com.pavle.wwoddsmobile.data.odds

import retrofit2.HttpException
import java.io.IOException

interface OddsRepository {
    suspend fun getEuEvents(): List<EventDto>
}

class RealOddsRepository(
    private val api: OddsApi
) : OddsRepository {
    override suspend fun getEuEvents(): List<EventDto> {
        try {
            val map = api.getOdds()
            return map["eu"] ?: emptyList()
        } catch (e: HttpException) {
            throw RuntimeException("Server error (${e.code()})")
        } catch (e: IOException) {
            throw RuntimeException("Network error")
        }
    }
}
