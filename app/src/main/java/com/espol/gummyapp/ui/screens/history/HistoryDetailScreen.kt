package com.espol.gummyapp.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
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
import com.espol.gummyapp.ui.history.HistoryRecord
import com.espol.gummyapp.ui.screens.connection.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiPrimary
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDetailScreen(
    record: HistoryRecord,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onConnectionClick: () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            SideMenuContent(onHelpClick = {}, onCreditsClick = {}, onCloseClick = {})
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
                        .padding(16.dp),
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
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                scope.launch { drawerState.open() }
                            })
                }

                Text(
                    text = "Historial",
                    color = GomiTextPrimary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {

                    Text(
                        text = record.mode,
                        color = GomiTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = formatDate(record.dateMillis),
                        fontSize = 16.sp,
                        color = GomiTextPrimary
                    )

                    Text(
                        text = formatTime(record.dateMillis),
                        fontSize = 16.sp,
                        color = GomiTextPrimary
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer),
                            contentDescription = null,
                            tint = GomiPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = formatDuration(record.durationSeconds),
                            color = GomiTextPrimary,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_warning),
                            contentDescription = null,
                            tint = GomiPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = record.errors.toString(),
                            color = GomiTextPrimary,
                            fontSize = 18.sp
                        )
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
                    selected = true,
                    onClick = {})

                BottomItem(
                    icon = R.drawable.ic_bluetooth,
                    label = "Conexión",
                    selected = false,
                    onClick = onConnectionClick
                )
            }
        }
    }
}

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("EEEE, d 'de' MMM", Locale("es"))
    return sdf.format(Date(millis)).replaceFirstChar { it.uppercase() }
}

fun formatTime(millis: Long): String {
    val sdf = SimpleDateFormat("HH'h'mm", Locale.getDefault())
    return sdf.format(Date(millis))
}

fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes}m ${remainingSeconds}s"
}