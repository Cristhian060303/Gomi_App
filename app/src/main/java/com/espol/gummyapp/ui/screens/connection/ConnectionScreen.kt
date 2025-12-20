package com.espol.gummyapp.ui.screens.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiBackgroundAlt
import com.espol.gummyapp.ui.theme.GomiPrimary
import com.espol.gummyapp.ui.theme.GomiTextPrimary

@Composable
fun ConnectionScreen(
    isBluetoothEnabled: Boolean,
    devices: List<BleDevice>,
    onToggleBluetooth: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GomiBackground)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            /* -------- TOP BAR -------- */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    tint = GomiTextPrimary,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })

                Icon(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Menú",
                    tint = GomiTextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = "Conexión",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = GomiTextPrimary,
                modifier = Modifier.padding(start = 16.dp, bottom = 20.dp)
            )

            /* -------- BLUETOOTH SWITCH -------- */

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bluetooth",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GomiTextPrimary
                    )

                    Switch(
                        checked = isBluetoothEnabled,
                        onCheckedChange = onToggleBluetooth,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = GomiPrimary,
                            checkedThumbColor = Color.White,
                            uncheckedTrackColor = GomiBackgroundAlt,
                            uncheckedThumbColor = Color.White
                        )
                    )
                }
            }

            /* -------- INFO -------- */

            Row(
                modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = null,
                    tint = GomiTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Active el bluetooth y la ubicación para ver los juguetes disponibles",
                    fontSize = 14.sp,
                    color = GomiTextPrimary
                )
            }

            /* -------- DEVICE LIST -------- */

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Juguetes disponibles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GomiTextPrimary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    when {
                        !isBluetoothEnabled -> {
                            Text(
                                text = "Bluetooth o Ubicación desactivados, porfavor recuerde activarlos", fontSize = 14.sp, color = Color.Gray
                            )
                        }

                        devices.isEmpty() -> {
                            Text(
                                text = "Buscando dispositivos...",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            ) {
                                items(devices) { device ->
                                    BleDeviceItem(
                                        deviceName = device.name,
                                        deviceAddress = device.address,
                                        onClick = {
                                            // aquí conectaremos luego
                                        })
                                }
                            }
                        }
                    }
                }
            }
        }

        /* -------- BOTTOM BAR -------- */

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(GomiBackground)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem(
                icon = R.drawable.ic_home, label = "Inicio", selected = false, onClick = onHomeClick
            )

            BottomItem(
                icon = R.drawable.ic_history,
                label = "Historial",
                selected = false,
                onClick = onHistoryClick
            )

            BottomItem(
                icon = R.drawable.ic_bluetooth, label = "Conexión", selected = true, onClick = {})
        }
    }
}

/* -------- COMPONENTS -------- */

@Composable
fun BottomItem(
    icon: Int, label: String, selected: Boolean, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 96.dp, height = 64.dp)
            .background(
                color = if (selected) GomiBackgroundAlt else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = GomiTextPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 12.sp, color = GomiTextPrimary)
        }
    }
}

@Composable
fun BleDeviceItem(
    deviceName: String, deviceAddress: String, onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = deviceName,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = GomiTextPrimary
            )
            Text(
                text = deviceAddress, fontSize = 12.sp, color = GomiTextPrimary.copy(alpha = 0.6f)
            )
        }
    }
}
