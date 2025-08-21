package com.pavle.wwoddsmobile.data.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): TokenResponse

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): TokenResponse
}
