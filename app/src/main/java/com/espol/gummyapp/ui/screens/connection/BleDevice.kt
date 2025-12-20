package com.espol.gummyapp.ui.screens.connection

enum class DeviceConnectionState {
    IDLE, CONNECTING, CONNECTED
}

data class BleDevice(
    val name: String,
    val address: String,
    val state: DeviceConnectionState = DeviceConnectionState.IDLE
)
