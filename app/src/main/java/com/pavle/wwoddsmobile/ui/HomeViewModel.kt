package com.pavle.wwoddsmobile.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavle.wwoddsmobile.data.auth.Network
import com.pavle.wwoddsmobile.data.odds.EventDto
import com.pavle.wwoddsmobile.data.odds.RealOddsRepository
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val events: List<EventDto> = emptyList()
)

class HomeViewModel : ViewModel() {
    private val repo = RealOddsRepository(Network.oddsApi)
    var ui by mutableStateOf(HomeUiState())
        private set

    fun load() { if (ui.events.isEmpty() && !ui.isLoading) fetch() }
    fun refresh() = fetch()

    private fun fetch() {
        viewModelScope.launch {
            ui = ui.copy(isLoading = true, error = null)
            runCatching { repo.getEuEvents() }
                .onSuccess { list -> ui = ui.copy(events = list) }
                .onFailure { err -> ui = ui.copy(error = err.message ?: "Something went wrong") }
            ui = ui.copy(isLoading = false)
        }
    }
}
