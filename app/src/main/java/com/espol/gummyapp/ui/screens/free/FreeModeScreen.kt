package com.espol.gummyapp.ui.screens.free

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.screens.home.BottomItem
import com.espol.gummyapp.ui.screens.memory.MemoryPiece
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiTextPrimary

@Composable
fun FreeModeScreen(
    pieces: List<MemoryPiece>,
    bleResponse: String?,
    isBleConnected: Boolean,
    onClearBleResponse: () -> Unit,
    onFinish: (errors: Int, durationSeconds: Int) -> Unit,
    onCancel: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onRecordClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onInterrupt: () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            onInterrupt()
        }
    }

    val activatedPieces = remember { mutableSetOf<String>() }
    var errorCount by remember { mutableStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    val startTime = remember { System.currentTimeMillis() }

    LaunchedEffect(bleResponse) {
        when (bleResponse) {

            "ERROR" -> {
                errorCount++
                onClearBleResponse()
            }

            else -> {
                val pieceId = colorToPieceId(bleResponse)
                if (pieceId != null) {
                    activatedPieces.add(pieceId)
                    onClearBleResponse()
                }
            }
        }

        if (!finished && activatedPieces.size == pieces.size) {
            finished = true
            val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
            onFinish(errorCount, duration)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GomiBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, top = 16.dp, start = 16.dp, end = 16.dp)
        ) {

            /* 🔝 TOP BAR */
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Modo Libre",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = GomiTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Interactúa libremente",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GomiTextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_loading),
                    contentDescription = "Loading",
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pieces.forEach { piece ->
                        Image(
                            painter = painterResource(id = piece.iconRes),
                            contentDescription = piece.name,
                            modifier = Modifier
                                .size(48.dp)
                                .alpha(
                                    if (activatedPieces.contains(piece.id)) 1f else 0.25f
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Cancelar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
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
                    icon = R.drawable.ic_bluetooth, label = "Conexión", onClick = onConnectionClick
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

private fun colorToPieceId(color: String?): String? = when (color) {
    "MORADO" -> "head"
    "AMARILLO" -> "tail"
    "CELESTE" -> "blue_leg"
    "NARANJA" -> "orange_leg"
    "ROSA" -> "pink_leg"
    "VERDE" -> "green_leg"
    else -> null
}