package com.espol.gummyapp.ui.screens.memory

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.components.SideMenuContent
import com.espol.gummyapp.ui.screens.home.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiBackgroundAlt
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch


@Composable
fun MemorySequenceScreen(
    isBleConnected: Boolean,
    onSendSequence: (List<MemoryPiece>) -> Unit,
    onHomeClick: () -> Unit,
    onRecordClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onBackClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseApp: () -> Unit
) {
    var pieces by remember { mutableStateOf(memoryPieces) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun moveUp(index: Int) {
        if (index <= 0) return
        pieces = pieces.toMutableList().apply {
            val item = removeAt(index)
            add(index - 1, item)
        }
    }

    fun moveDown(index: Int) {
        if (index >= pieces.lastIndex) return
        pieces = pieces.toMutableList().apply {
            val item = removeAt(index)
            add(index + 1, item)
        }
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {

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
                            .clickable { scope.launch { drawerState.open() } })
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Memoria",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = GomiTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Crea una secuencia",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GomiTextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        itemsIndexed(pieces, key = { _, item -> item.id }) { index, piece ->
                            MemoryPieceRow(
                                piece = piece,
                                canMoveUp = index > 0,
                                canMoveDown = index < pieces.lastIndex,
                                onMoveUp = { moveUp(index) },
                                onMoveDown = { moveDown(index) })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Button(
                        onClick = {
                            onSendSequence(pieces)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Continuar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(GomiBackground)
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                BottomItem(
                    icon = R.drawable.ic_home, label = "Inicio", onClick = onHomeClick
                )

                BottomItem(
                    icon = R.drawable.ic_history, label = "Historial", onClick = onRecordClick
                )

                Box {
                    BottomItem(
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

@Composable
private fun MemoryPieceRow(
    piece: MemoryPiece,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GomiBackgroundAlt, RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = piece.iconRes),
                contentDescription = piece.name,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = piece.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = GomiTextPrimary
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_up),
                contentDescription = "Subir",
                tint = if (canMoveUp) GomiTextPrimary else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(enabled = canMoveUp) { onMoveUp() })

            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "Bajar",
                tint = if (canMoveDown) GomiTextPrimary else Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(enabled = canMoveDown) { onMoveDown() })
        }
    }
}
