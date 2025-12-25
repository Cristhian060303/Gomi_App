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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.components.SideMenuContent
import com.espol.gummyapp.ui.history.HistoryRecord
import com.espol.gummyapp.ui.history.HistoryStorage
import com.espol.gummyapp.ui.screens.connection.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiBackgroundAlt
import com.espol.gummyapp.ui.theme.GomiPrimary
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch

@Composable
fun HistoryListScreen(
    isBleConnected: Boolean,
    onItemClick: (HistoryRecord) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onRecordClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseApp: () -> Unit
) {
    val context = LocalContext.current
    var records by remember { mutableStateOf<List<HistoryRecord>>(emptyList()) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        records = HistoryStorage.getRecords(context)
    }

    val grouped = remember(records) {
        groupByMonthAndDay(
            records.sortedByDescending { it.dateMillis })
    }

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            SideMenuContent(
                onHelpClick = {}, onCreditsClick = onCreditsClick, onCloseClick = onCloseApp
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
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Volver",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onBackClick() })

                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "Menú",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { scope.launch { drawerState.open() } })
                }

                Text(
                    text = "Historial",
                    fontSize = 40.sp,
                    color = GomiTextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {

                    LazyColumn {

                        grouped.forEach { (month, daysMap) ->

                            item {
                                Text(
                                    text = capitalizeFirstLetter(month),
                                    color = GomiTextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp
                                )
                                Spacer(Modifier.height(12.dp))
                            }

                            daysMap.forEach { (day, dayRecords) ->
                                item {
                                    HistoryDayGroup(
                                        day = day, records = dayRecords, onItemClick = onItemClick
                                    )
                                    Spacer(Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
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
                        onClick = onRecordClick
                    )

                    Box {
                        com.espol.gummyapp.ui.screens.home.BottomItem(
                            icon = R.drawable.ic_bluetooth,
                            label = "Conexión",
                            onClick = onConnectionClick
                        )

                        if (!isBleConnected) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color.Red, CircleShape)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryDayItem(
    record: HistoryRecord, showDayNumber: Boolean, dayNumber: Int, onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() }) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showDayNumber) {
                Text(
                    text = dayNumber.toString(),
                    fontWeight = FontWeight.Bold,
                    color = GomiTextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(GomiPrimary)
            )
        }

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GomiBackgroundAlt, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = record.mode,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = GomiTextPrimary
            )
        }
    }
}

fun groupByMonthAndDay(
    records: List<HistoryRecord>
): Map<String, Map<Int, List<HistoryRecord>>> {

    val calendar = java.util.Calendar.getInstance()

    return records.groupBy { record ->
        calendar.timeInMillis = record.dateMillis
        calendar.getDisplayName(
            java.util.Calendar.MONTH, java.util.Calendar.LONG, java.util.Locale.getDefault()
        ) ?: "Mes"
    }.mapValues { (_, monthRecords) ->
        monthRecords.groupBy { record ->
            calendar.timeInMillis = record.dateMillis
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        }
    }
}

fun capitalizeFirstLetter(text: String): String {
    return text.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase(java.util.Locale.getDefault())
        else char.toString()
    }
}

@Composable
fun HistoryDayGroup(
    day: Int, records: List<HistoryRecord>, onItemClick: (HistoryRecord) -> Unit
) {
    val itemHeight = 48.dp
    val spacing = 8.dp
    val lineHeight = (records.size * itemHeight) + ((records.size - 1).coerceAtLeast(0) * spacing)

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(32.dp)
        ) {

            Text(
                text = day.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = GomiTextPrimary
            )

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(lineHeight)
                    .background(GomiPrimary)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(spacing), modifier = Modifier.weight(1f)
        ) {
            records.forEach { record ->
                HistoryRecordItem(
                    record = record, onClick = { onItemClick(record) })
            }
        }
    }
}


@Composable
fun HistoryRecordItem(
    record: HistoryRecord, onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GomiBackgroundAlt, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)) {
        Text(
            text = record.mode,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = GomiTextPrimary
        )
    }
}
