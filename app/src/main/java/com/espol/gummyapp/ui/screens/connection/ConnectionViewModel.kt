package com.espol.gummyapp.ui.screens.connection

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("MissingPermission")
class ConnectionViewModel(
    bluetoothAdapter: BluetoothAdapter?
) : ViewModel() {

    private val _isBluetoothEnabled = MutableStateFlow(bluetoothAdapter?.isEnabled == true)
    val isBluetoothEnabled = _isBluetoothEnabled

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val devices = _devices

    private val scanner = bluetoothAdapter?.bluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(type: Int, result: ScanResult) {
            val name = result.device.name ?: return
            val device = BleDevice(name, result.device.address)

            if (_devices.value.none { it.address == device.address }) {
                _devices.value = _devices.value + device
            }
        }
    }

    init {
        if (_isBluetoothEnabled.value) {
            scanner?.startScan(scanCallback)
        }
    }

    override fun onCleared() {
        scanner?.stopScan(scanCallback)
        super.onCleared()
    }
}

