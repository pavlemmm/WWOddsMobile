package com.pavle.wwoddsmobile.data.auth

import retrofit2.HttpException
import java.io.IOException

interface AuthRepository {
    suspend fun signIn(email: String, password: String)
    suspend fun register(firstName: String, lastName: String, email: String, password: String)
}

class RealAuthRepository(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun signIn(email: String, password: String) {
        try {
            val res = api.login(LoginRequest(email.trim(), password))
            Session.setToken(res.token.trim())
        } catch (e: HttpException) {
            throw mapHttp(e)
        } catch (e: IOException) {
            throw RuntimeException("Network error. Check your connection.")
        }
    }

    override suspend fun register(firstName: String, lastName: String, email: String, password: String) {
        try {
            val body = RegisterRequest(
                firstName = firstName.trim(),
                lastName  = lastName.trim(),
                email     = email.trim(),
                password  = password,
                regions   = listOf("eu")
            )
            val res = api.register(body)
            Session.setToken(res.token.trim())
        } catch (e: HttpException) {
            throw mapHttp(e)
        } catch (e: IOException) {
            throw RuntimeException("Network error. Check your connection.")
        }
    }

    private fun mapHttp(e: HttpException): RuntimeException {
        val body = try { e.response()?.errorBody()?.string()?.take(300) } catch (_: Throwable) { null }
        val msg = buildString {
            append("HTTP ").append(e.code())
            e.message()?.let { append(" • ").append(it) }
            body?.let { append("\n").append(it) }
        }
        return RuntimeException(msg.ifBlank { "HTTP ${e.code()}" })
    }
}
