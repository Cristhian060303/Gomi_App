package com.espol.gummyapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.espol.gummyapp.ui.screens.connection.BleDevice
import com.espol.gummyapp.ui.screens.connection.ConnectionScreen
import com.espol.gummyapp.ui.screens.home.HomeScreen
import com.espol.gummyapp.ui.screens.welcome.WelcomeScreen
import com.espol.gummyapp.ui.theme.GummyAppTheme

sealed class Screen {
    object Welcome : Screen()
    object Home : Screen()
    object DeviceScan : Screen()
}

class MainActivity : ComponentActivity() {

    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

        setContent {
            GummyAppTheme {
                GummyApp(
                    onRequestEnableBluetooth = { requestEnableBluetooth() },
                    onOpenLocationSettings = { openLocationSettings() })
            }
        }
    }

    private fun requestEnableBluetooth() {
        enableBluetoothLauncher.launch(
            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        )
    }

    private fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}

@SuppressLint("MissingPermission")
@Composable
fun GummyApp(
    onRequestEnableBluetooth: () -> Unit, onOpenLocationSettings: () -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    val context = LocalContext.current

    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val scanner = bluetoothAdapter?.bluetoothLeScanner

    val discoveredDevices = remember { mutableStateListOf<BleDevice>() }
    var isScanning by remember { mutableStateOf(false) }

    /* -------- PERMISOS -------- */

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    var hasPermissions by remember {
        mutableStateOf(
            permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            })
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        hasPermissions = it.values.all { granted -> granted }
    }

    LaunchedEffect(Unit) {
        if (!hasPermissions) permissionLauncher.launch(permissions)
    }

    /* -------- ESTADO REAL BLUETOOTH -------- */

    var isBluetoothEnabled by remember {
        mutableStateOf(bluetoothAdapter?.isEnabled == true)
    }

    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context?, intent: Intent?) {
                if (intent?.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                    val state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR
                    )
                    isBluetoothEnabled = state == BluetoothAdapter.STATE_ON
                }
            }
        }

        context.registerReceiver(
            receiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        )

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    /* -------- BLE SCAN CALLBACK -------- */

    val scanCallback = remember {
        object : ScanCallback() {
            override fun onScanResult(
                callbackType: Int, result: ScanResult
            ) {
                val name = result.device.name ?: return
                val address = result.device.address

                if (discoveredDevices.none { it.address == address }) {
                    discoveredDevices.add(
                        BleDevice(
                            name = name, address = address
                        )
                    )
                }
            }
        }
    }

    /* -------- CONTROL DE SCAN -------- */

    LaunchedEffect(
        currentScreen, isBluetoothEnabled, hasPermissions
    ) {
        val shouldScan = currentScreen is Screen.DeviceScan && isBluetoothEnabled && hasPermissions

        if (shouldScan && !isScanning) {
            discoveredDevices.clear()
            isScanning = true
            scanner?.startScan(scanCallback)
        }

        if (!shouldScan && isScanning) {
            isScanning = false
            scanner?.stopScan(scanCallback)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isScanning) {
                scanner?.stopScan(scanCallback)
            }
        }
    }

    /* -------- UI -------- */

    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        Crossfade(targetState = currentScreen, label = "") { screen ->
            when (screen) {

                Screen.Welcome -> WelcomeScreen {
                    currentScreen = Screen.Home
                }

                Screen.Home -> HomeScreen(
                    isBleConnected = isBluetoothEnabled,
                    onStartClick = {},
                    onHistoryClick = {},
                    onConnectionClick = {
                        currentScreen = Screen.DeviceScan
                    },
                    onBackClick = {
                        currentScreen = Screen.Welcome
                    })

                Screen.DeviceScan -> ConnectionScreen(
                    isBluetoothEnabled = isBluetoothEnabled,
                    devices = discoveredDevices,
                    onToggleBluetooth = { enabled ->
                        if (enabled) {
                            onRequestEnableBluetooth()
                            onOpenLocationSettings()
                        }
                    },
                    onBackClick = { currentScreen = Screen.Home },
                    onHomeClick = { currentScreen = Screen.Home },
                    onHistoryClick = {})
            }
        }
    }
}
