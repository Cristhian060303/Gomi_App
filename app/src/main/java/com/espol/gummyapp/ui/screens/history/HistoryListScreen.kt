package com.espol.gummyapp.ui.screens.history

import androidx.compose.runtime.Composable
import com.espol.gummyapp.ui.history.HistoryRecord

@Composable
fun HistoryListScreen(
    records: List<HistoryRecord>,
    onItemClick: (HistoryRecord) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onConnectionClick: () -> Unit
) {
    // Barra superior (← atrás)
    // Caja blanca
    // Lista (LazyColumn)
    // Bottom bar (igual a las otras)
}
