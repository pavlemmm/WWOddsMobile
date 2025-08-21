package com.pavle.wwoddsmobile.data.auth

object Session {
    @Volatile var token: String? = null
        private set

    fun setToken(value: String) { token = value }
    fun clear() { token = null }
}
