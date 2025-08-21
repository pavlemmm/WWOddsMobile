package com.pavle.wwoddsmobile.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavle.wwoddsmobile.data.auth.Network
import com.pavle.wwoddsmobile.data.auth.RealAuthRepository
import kotlinx.coroutines.launch

data class AuthUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val repo = RealAuthRepository(Network.authApi)

    var ui by mutableStateOf(AuthUiState())
        private set

    fun onFirstName(v: String)     { ui = ui.copy(firstName = v, error = null) }
    fun onLastName(v: String)      { ui = ui.copy(lastName = v,  error = null) }
    fun onEmailChange(v: String)   { ui = ui.copy(email = v,     error = null) }
    fun onPasswordChange(v: String){ ui = ui.copy(password = v,  error = null) }
    fun onConfirmChange(v: String) { ui = ui.copy(confirm = v,   error = null) }

    fun signIn(onSuccess: () -> Unit) {
        if (ui.email.isBlank() || ui.password.isBlank()) { ui = ui.copy(error = "Please fill in all fields"); return }
        if (!ui.email.contains("@")) { ui = ui.copy(error = "Invalid email"); return }

        viewModelScope.launch {
            ui = ui.copy(isLoading = true, error = null)
            runCatching { repo.signIn(ui.email, ui.password) }
                .onSuccess { onSuccess() }
                .onFailure { err -> ui = ui.copy(error = err.message ?: "Error") }
            ui = ui.copy(isLoading = false)
        }
    }

    fun register(onSuccess: () -> Unit) {
        if (ui.firstName.isBlank() || ui.lastName.isBlank() ||
            ui.email.isBlank() || ui.password.isBlank() || ui.confirm.isBlank()
        ) { ui = ui.copy(error = "Please fill in all fields"); return }
        if (!ui.email.contains("@")) { ui = ui.copy(error = "Invalid email"); return }
        if (ui.password.length < 6) { ui = ui.copy(error = "Password must be at least 6 characters"); return }
        if (ui.password != ui.confirm) { ui = ui.copy(error = "Passwords do not match"); return }

        viewModelScope.launch {
            ui = ui.copy(isLoading = true, error = null)
            runCatching { repo.register(ui.firstName, ui.lastName, ui.email, ui.password) }
                .onSuccess { onSuccess() }
                .onFailure { err -> ui = ui.copy(error = err.message ?: "Error") }
            ui = ui.copy(isLoading = false)
        }
    }
}
