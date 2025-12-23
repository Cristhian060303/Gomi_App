package com.espol.gummyapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
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
import com.espol.gummyapp.ui.history.HistoryRecord
import com.espol.gummyapp.ui.history.HistoryStorage
import com.espol.gummyapp.ui.screens.connection.BleDevice
import com.espol.gummyapp.ui.screens.connection.ConnectionScreen
import com.espol.gummyapp.ui.screens.connection.DeviceConnectionState
import com.espol.gummyapp.ui.screens.credits.CreditsScreen
import com.espol.gummyapp.ui.screens.game.GameModeScreen
import com.espol.gummyapp.ui.screens.history.HistoryDetailScreen
import com.espol.gummyapp.ui.screens.history.HistoryListScreen
import com.espol.gummyapp.ui.screens.home.HomeScreen
import com.espol.gummyapp.ui.screens.story.StoryColorsScreen
import com.espol.gummyapp.ui.screens.story.StoryCombinedScreen
import com.espol.gummyapp.ui.screens.story.StoryCompletedScreen
import com.espol.gummyapp.ui.screens.story.StoryFormsScreen
import com.espol.gummyapp.ui.screens.story.StorySelectionScreen
import com.espol.gummyapp.ui.screens.welcome.WelcomeScreen
import com.espol.gummyapp.ui.theme.GummyAppTheme
import java.util.UUID

// --- CONFIGURACION BLE ---
val SERVICE_UUID: UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")

val CHARACTERISTIC_WRITE_UUID: UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E")

val CHARACTERISTIC_NOTIFY_UUID: UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E")

sealed class Screen {
    object Welcome : Screen()
    object Home : Screen()
    object DeviceScan : Screen()
    object Credits : Screen()
    object GameMode : Screen()
    object StorySelection : Screen()
    object StoryColors : Screen()
    object StoryForms : Screen()
    object StoryCombined : Screen()
    data class StoryCompleted(
        val modeName: String, val totalErrors: Int, val totalTimeSeconds: Int
    ) : Screen()

    object HistoryList : Screen()
    data class HistoryDetail(val record: HistoryRecord) : Screen()

}

class MainActivity : ComponentActivity() {

    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableBluetoothLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

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

    // --- ESTADO BLE ---
    var bluetoothGatt by remember { mutableStateOf<BluetoothGatt?>(null) }
    var bleResponse by remember { mutableStateOf<String?>(null) }

    // Características BLE
    var writeCharacteristic by remember {
        mutableStateOf<BluetoothGattCharacteristic?>(null)
    }
    var notifyCharacteristic by remember {
        mutableStateOf<BluetoothGattCharacteristic?>(null)
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    val context = LocalContext.current

    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val scanner = bluetoothAdapter?.bluetoothLeScanner

    val discoveredDevices = remember { mutableStateListOf<BleDevice>() }
    var isScanning by remember { mutableStateOf(false) }
    var isBluetoothEnabled by remember {
        mutableStateOf(bluetoothAdapter?.isEnabled == true)
    }

    discoveredDevices.any {
        it.name == "Gomi FADCOM_2025" && it.state == DeviceConnectionState.CONNECTED
    }

    var isDeviceConnected by remember { mutableStateOf(false) }

    val gattCallback = remember {
        object : BluetoothGattCallback() {

            override fun onConnectionStateChange(
                gatt: BluetoothGatt, status: Int, newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    bluetoothGatt = gatt

                    val index = discoveredDevices.indexOfFirst {
                        it.address == gatt.device.address
                    }

                    if (index != -1) {
                        discoveredDevices[index] = discoveredDevices[index].copy(
                            state = DeviceConnectionState.CONNECTED
                        )
                        isDeviceConnected = true
                    }
                    gatt.discoverServices()
                }

                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    bluetoothGatt?.close()
                    bluetoothGatt = null

                    val index = discoveredDevices.indexOfFirst {
                        it.address == gatt.device.address
                    }

                    if (index != -1) {
                        discoveredDevices[index] = discoveredDevices[index].copy(
                            state = DeviceConnectionState.IDLE
                        )
                    }
                    isDeviceConnected = false
                    currentScreen = Screen.Home
                }
            }

            override fun onServicesDiscovered(
                gatt: BluetoothGatt, status: Int
            ) {
                val service = gatt.getService(SERVICE_UUID) ?: return

                writeCharacteristic = service.getCharacteristic(CHARACTERISTIC_WRITE_UUID)

                notifyCharacteristic = service.getCharacteristic(CHARACTERISTIC_NOTIFY_UUID)

                gatt.setCharacteristicNotification(notifyCharacteristic, true)

                val descriptor = notifyCharacteristic?.getDescriptor(
                    UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
                )

                descriptor?.let {
                    it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    gatt.writeDescriptor(it)
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic
            ) {
                val message = characteristic.value.toString(Charsets.UTF_8).trim()

                bleResponse = message
            }
        }
    }

    fun sendColorToEsp32(color: String) {
        if (writeCharacteristic == null || bluetoothGatt == null) {
            Toast.makeText(
                context, "BLE no listo aún", Toast.LENGTH_SHORT
            ).show()
            return
        }

        bleResponse = null
        writeCharacteristic?.let {
            it.value = color.toByteArray()
            bluetoothGatt?.writeCharacteristic(it)
        }
    }

    fun closeApp() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null

        (context as? ComponentActivity)?.finish()
    }

    DisposableEffect(Unit) {
        onDispose {
            bluetoothGatt?.let { gatt ->
                try {
                    gatt.disconnect()
                    gatt.close()
                } catch (_: Exception) {
                }
                bluetoothGatt = null
            }

            for (i in discoveredDevices.indices) {
                discoveredDevices[i] = discoveredDevices[i].copy(
                    state = DeviceConnectionState.IDLE
                )
            }
        }
    }

    // Escucha cambios reales del Bluetooth del sistema
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(c: Context, intent: Intent) {
                if (intent.action == BluetoothAdapter.ACTION_STATE_CHANGED) {
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

    // Permisos
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
                ContextCompat.checkSelfPermission(
                    context, it
                ) == PackageManager.PERMISSION_GRANTED
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

    // BLE Scan callback
    val scanCallback = remember {
        object : android.bluetooth.le.ScanCallback() {
            override fun onScanResult(
                callbackType: Int, result: android.bluetooth.le.ScanResult
            ) {
                val name = result.device.name ?: return
                val address = result.device.address

                if (discoveredDevices.none { it.address == address }) {
                    discoveredDevices.add(BleDevice(name, address))
                }
            }
        }
    }

    // Control de escaneo
    LaunchedEffect(currentScreen, isBluetoothEnabled, hasPermissions) {
        val shouldScan = currentScreen is Screen.DeviceScan && isBluetoothEnabled && hasPermissions

        if (shouldScan && !isScanning) {
            isScanning = true
            scanner?.startScan(scanCallback)
        }

        if (!shouldScan && isScanning) {
            isScanning = false
            scanner?.stopScan(scanCallback)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Crossfade(targetState = currentScreen, label = "") { screen ->
            when (screen) {
                Screen.Welcome -> WelcomeScreen {
                    currentScreen = Screen.Home
                }

                Screen.Home -> HomeScreen(
                    isBleConnected = isDeviceConnected,
                    onStartClick = {
                        if (!isDeviceConnected) {
                            Toast.makeText(
                                context,
                                "Debes conectarte al juguete para comenzar",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            currentScreen = Screen.GameMode
                        }
                    },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onCreditsClick = { currentScreen = Screen.Credits },
                    onCloseApp = { closeApp() },
                    onBackClick = { currentScreen = Screen.Welcome })

                Screen.Credits -> CreditsScreen {
                    currentScreen = Screen.Home
                }

                Screen.DeviceScan -> ConnectionScreen(
                    isBluetoothEnabled = isBluetoothEnabled,
                    devices = discoveredDevices,
                    onToggleBluetooth = { enabled ->
                        if (enabled) {
                            onRequestEnableBluetooth()
                            onOpenLocationSettings()
                        }
                    },
                    onDeviceClick = { device ->

                        if (device.name != "Gomi FADCOM_2025") return@ConnectionScreen

                        val index = discoveredDevices.indexOfFirst {
                            it.address == device.address
                        }

                        if (index == -1) return@ConnectionScreen

                        when (device.state) {

                            DeviceConnectionState.CONNECTED -> {

                                discoveredDevices[index] = discoveredDevices[index].copy(
                                    state = DeviceConnectionState.CONNECTING
                                )

                                bluetoothGatt?.disconnect()
                            }

                            DeviceConnectionState.IDLE -> {
                                discoveredDevices[index] = discoveredDevices[index].copy(
                                    state = DeviceConnectionState.CONNECTING
                                )

                                val bluetoothDevice =
                                    bluetoothAdapter?.getRemoteDevice(device.address)

                                bluetoothGatt = bluetoothDevice?.connectGatt(
                                    context, false, gattCallback
                                )
                            }

                            DeviceConnectionState.CONNECTING -> {
                                bluetoothGatt?.disconnect()

                                discoveredDevices[index] = discoveredDevices[index].copy(
                                    state = DeviceConnectionState.IDLE
                                )
                            }
                        }
                    },
                    onCreditsClick = { currentScreen = Screen.Credits },
                    onBackClick = { currentScreen = Screen.Home },
                    onHomeClick = { currentScreen = Screen.Home },
                    onCloseApp = { closeApp() },
                    onRecordClick = { currentScreen = Screen.HistoryList })

                Screen.GameMode -> GameModeScreen(
                    onHistoryClick = {
                    currentScreen = Screen.StorySelection
                },
                    onMemoryClick = {
                        // más adelante: modo memoria
                    },
                    onFreeClick = {
                        // más adelante: modo libre
                    },
                    onHomeClick = {
                        currentScreen = Screen.Home
                    },
                    onConnectionClick = {
                        currentScreen = Screen.DeviceScan
                    },
                    onBackClick = {
                        currentScreen = Screen.Home
                    },
                    onCreditsClick = { currentScreen = Screen.Credits },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onCloseApp = { closeApp() })

                Screen.StorySelection -> StorySelectionScreen(
                    isBleConnected = isDeviceConnected,
                    onColorsClick = { currentScreen = Screen.StoryColors },
                    onFormsClick = { currentScreen = Screen.StoryForms },
                    onCombinedClick = { currentScreen = Screen.StoryCombined },
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onCreditsClick = { currentScreen = Screen.Credits },
                    onCloseApp = { closeApp() },
                    onBackClick = { currentScreen = Screen.GameMode })

                Screen.StoryColors -> StoryColorsScreen(
                    isBleConnected = isDeviceConnected,
                    bleResponse = bleResponse,
                    onActivatePieces = { color ->
                        sendColorToEsp32(color)
                    },
                    onStoryCompleted = { modeName, totalErrors, totalTime ->
                        HistoryStorage.saveRecord(
                            context = context, record = HistoryRecord(
                                mode = modeName,
                                dateMillis = System.currentTimeMillis(),
                                durationSeconds = totalTime,
                                errors = totalErrors
                            )
                        )

                        currentScreen = Screen.StoryCompleted(
                            modeName = modeName,
                            totalErrors = totalErrors,
                            totalTimeSeconds = totalTime
                        )
                    },
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onBackClick = { currentScreen = Screen.StorySelection })

                Screen.StoryForms -> StoryFormsScreen(
                    isBleConnected = isDeviceConnected,
                    bleResponse = bleResponse,
                    onActivatePieces = { color ->
                        sendColorToEsp32(color)
                    },
                    onStoryCompleted = { modeName, totalErrors, totalTime ->
                        HistoryStorage.saveRecord(
                            context = context, record = HistoryRecord(
                                mode = modeName,
                                dateMillis = System.currentTimeMillis(),
                                durationSeconds = totalTime,
                                errors = totalErrors
                            )
                        )

                        currentScreen = Screen.StoryCompleted(
                            modeName = modeName,
                            totalErrors = totalErrors,
                            totalTimeSeconds = totalTime
                        )
                    },
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onBackClick = { currentScreen = Screen.StorySelection })

                Screen.StoryCombined -> StoryCombinedScreen(
                    isBleConnected = isDeviceConnected,
                    bleResponse = bleResponse,
                    onActivatePieces = { color ->
                        sendColorToEsp32(color)
                    },
                    onStoryCompleted = { modeName, totalErrors, totalTime ->
                        HistoryStorage.saveRecord(
                            context = context, record = HistoryRecord(
                                mode = modeName,
                                dateMillis = System.currentTimeMillis(),
                                durationSeconds = totalTime,
                                errors = totalErrors
                            )
                        )

                        currentScreen = Screen.StoryCompleted(
                            modeName = modeName,
                            totalErrors = totalErrors,
                            totalTimeSeconds = totalTime
                        )
                    },
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onBackClick = { currentScreen = Screen.StorySelection })

                is Screen.StoryCompleted -> StoryCompletedScreen(
                    modeName = screen.modeName,
                    totalTimeSeconds = screen.totalTimeSeconds,
                    totalErrors = screen.totalErrors,
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onBackClick = { currentScreen = Screen.Home })

                Screen.HistoryList -> HistoryListScreen(
                    isBleConnected = isDeviceConnected,
                    onItemClick = { record ->
                        currentScreen = Screen.HistoryDetail(record)
                    },
                    onBackClick = { currentScreen = Screen.Home },
                    onHomeClick = { currentScreen = Screen.Home },
                    onRecordClick = { currentScreen = Screen.HistoryList },
                    onConnectionClick = { currentScreen = Screen.DeviceScan },
                    onCreditsClick = { currentScreen = Screen.Credits },
                    onCloseApp = { closeApp() })

                is Screen.HistoryDetail -> HistoryDetailScreen(
                    record = screen.record,
                    onBackClick = { currentScreen = Screen.HistoryList },
                    onHomeClick = { currentScreen = Screen.Home },
                    onConnectionClick = { currentScreen = Screen.DeviceScan })

            }
        }
    }
}