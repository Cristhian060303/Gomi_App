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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.components.SideMenuContent
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiBackgroundAlt
import com.espol.gummyapp.ui.theme.GomiPrimary
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(
    isBluetoothEnabled: Boolean,
    devices: List<BleDevice>,
    onToggleBluetooth: (Boolean) -> Unit,
    onDeviceClick: (BleDevice) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseApp: () -> Unit,
    onHistoryClick: () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            SideMenuContent(
                onHelpClick = { },
                onCreditsClick = onCreditsClick,
                onCloseClick = onCloseApp
            )
        }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GomiBackground)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

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
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                }
                            })
                }

                Text(
                    text = "Conexión",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = GomiTextPrimary,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 20.dp)
                )

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

                Row(
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isBluetoothEnabled) {
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
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {

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
                                    text = "Active el bluetooth para comenzar",
                                    fontSize = 14.sp,
                                    color = Color.Gray
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
                                    contentPadding = PaddingValues(bottom = 12.dp)
                                ) {
                                    items(devices) { device ->
                                        BleDeviceItem(
                                            device = device, onClick = { onDeviceClick(device) })
                                    }
                                }
                            }
                        }
                    }
                }
            }

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
                    icon = R.drawable.ic_home,
                    label = "Inicio",
                    selected = false,
                    onClick = onHomeClick
                )

                BottomItem(
                    icon = R.drawable.ic_history,
                    label = "Historial",
                    selected = false,
                    onClick = onHistoryClick
                )

                BottomItem(
                    icon = R.drawable.ic_bluetooth,
                    label = "Conexión",
                    selected = true,
                    onClick = { })
            }
        }
    }
}

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = GomiTextPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label, fontSize = 12.sp, color = GomiTextPrimary
            )
        }
    }
}

@Composable
fun BleDeviceItem(
    device: BleDevice, onClick: () -> Unit
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = device.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GomiTextPrimary
                    )
                    Text(
                        text = device.address,
                        fontSize = 12.sp,
                        color = GomiTextPrimary.copy(alpha = 0.6f)
                    )
                }

                when (device.state) {
                    DeviceConnectionState.CONNECTING -> {
                        Text("Conectando...", fontSize = 12.sp, color = Color.Gray)
                    }

                    DeviceConnectionState.CONNECTED -> {
                        Text(
                            "Conectado",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GomiPrimary
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

