package com.pavle.wwoddsmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pavle.wwoddsmobile.ui.HomeViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onLogout: (() -> Unit)? = null) {
    val vm: HomeViewModel = viewModel()
    val state = vm.ui

    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matches (EU)") },
                actions = {
                    TextButton(onClick = { vm.refresh() }, enabled = !state.isLoading) {
                        Text(if (state.isLoading) "Loading…" else "Refresh")
                    }
                    onLogout?.let {
                        TextButton(onClick = it, enabled = !state.isLoading) {
                            Text("Logout")
                        }
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(state.events, key = { _, e -> e.id }) { _, e ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(
                                "${e.homeTeam} vs ${e.awayTeam}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            e.sportTitle.takeIf { it.isNotBlank() }?.let {
                                Spacer(Modifier.height(2.dp))
                                Text(it, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                            }

                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Kickoff: ${formatKickoff(e.commenceTime)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            val lines = e.bookmakers
                                ?.mapNotNull { bm ->
                                    val outcomes = bm.markets?.firstOrNull { it.key.equals("h2h", true) }?.outcomes
                                        ?: return@mapNotNull null
                                    var h: Double? = null; var d: Double? = null; var a: Double? = null
                                    outcomes.forEach { o ->
                                        when {
                                            o.name.equals(e.homeTeam, true) -> h = o.price
                                            o.name.equals("Draw", true)      -> d = o.price
                                            o.name.equals(e.awayTeam, true) -> a = o.price
                                        }
                                    }
                                    val parts = buildList {
                                        h?.let { add("H ${"%.2f".format(it)}") }
                                        d?.let { add("D ${"%.2f".format(it)}") }
                                        a?.let { add("A ${"%.2f".format(it)}") }
                                    }
                                    if (parts.isEmpty()) null else "${bm.title}: ${parts.joinToString("  |  ")}"
                                }
                                ?.takeIf { it.isNotEmpty() }

                            Spacer(Modifier.height(8.dp))
                            if (lines != null) {
                                lines.forEachIndexed { idx, line ->
                                    if (idx > 0) HorizontalDivider(Modifier.padding(vertical = 6.dp))
                                    Text(
                                        line,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            } else {
                                Text("No odds", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatKickoff(iso: String): String = runCatching {
    val zdt = Instant.parse(iso).atZone(ZoneId.systemDefault())
    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm").format(zdt)
}.getOrElse { iso }
