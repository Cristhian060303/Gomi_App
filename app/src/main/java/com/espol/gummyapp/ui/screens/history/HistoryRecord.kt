package com.espol.gummyapp.ui.history

data class HistoryRecord(
    val id: Long = System.currentTimeMillis(),
    val mode: String,              // "Historia Colores", "Libre", etc
    val dateMillis: Long,           // Fecha completa
    val durationSeconds: Int,       // Tiempo total
    val errors: Int                 // Errores totales
)
