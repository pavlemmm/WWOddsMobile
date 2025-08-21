package com.pavle.wwoddsmobile.data.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName")  val lastName: String,
    val email: String,
    val password: String,
    val regions: List<String>
)

data class TokenResponse(
    @SerializedName("token") val token: String
)
