package com.espol.gummyapp.ui.screens.history

import androidx.compose.runtime.Composable
import com.espol.gummyapp.ui.history.HistoryRecord

@Composable
fun HistoryDetailScreen(
    record: HistoryRecord,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onConnectionClick: () -> Unit
) {
    // Barra superior (← atrás)
    // Caja blanca
    // Texto:
    // - Nombre del modo
    // - Fecha
    // - Hora
    // - Duración (⏱ icono)
    // - Errores (⚠ icono)
    // Bottom bar
}
