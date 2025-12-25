package com.espol.gummyapp.ui.history

data class HistoryRecord(
    val id: Long = System.currentTimeMillis(),
    val mode: String,
    val dateMillis: Long,
    val durationSeconds: Int,
    val errors: Int
)
