package com.espol.gummyapp.ui.screens.memory

import androidx.annotation.DrawableRes

data class MemoryPiece(
    val id: String, val name: String, val colorCommand: String, @DrawableRes val iconRes: Int
)