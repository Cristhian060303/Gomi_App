package com.espol.gummyapp.ui.screens.connection

data class BleDevice(
    val name: String,
    val address: String,
    val state: UiConnectionState = UiConnectionState.IDLE
)