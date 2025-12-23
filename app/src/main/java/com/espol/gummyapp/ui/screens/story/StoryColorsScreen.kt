package com.espol.gummyapp.ui.screens.story

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.screens.home.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiGreenLight
import com.espol.gummyapp.ui.theme.GomiTextPrimary

private val colorCommands = listOf(
    "MORADO", "ROSA", "CELESTE", "VERDE", "NARANJA", "AMARILLO"
)

@Composable
fun StoryColorsScreen(
    isBleConnected: Boolean,
    bleResponse: String?,
    onActivatePieces: (String) -> Unit,
    onStoryCompleted: (
        modeName: String, totalErrors: Int, totalTimeSeconds: Int
    ) -> Unit,
    onHomeClick: () -> Unit,
    onRecordClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onBackClick: () -> Unit,
    onInterrupt: () -> Unit
) {
    var hasFinishedStory by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            if (!hasFinishedStory) {
                onInterrupt()
            }
        }
    }

    var elapsedTime by remember { mutableStateOf(0) }
    var currentStep by remember { mutableStateOf(0) }
    var completedSteps by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var errorCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            elapsedTime++
        }
    }

    LaunchedEffect(bleResponse) {
        if (!showDialog || !isLoading) return@LaunchedEffect
        if (bleResponse == null) return@LaunchedEffect

        when (bleResponse) {
            "CORRECTO" -> {
                isLoading = false
                isCorrect = true
                completedSteps++
            }

            "ERROR" -> {
                isLoading = false
                isCorrect = false
                errorCount++
            }

            else -> {
                isLoading = false
                isCorrect = false
                errorCount++
            }
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
                .padding(bottom = 80.dp)
        ) {

            /* 🔝 Barra superior */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })
            }

            /* 📦 Caja blanca principal */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {

                /* 🟪 Caja 1 – Título */
                Box(
                    modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Colores",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = GomiTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                DividerGomi()
                Spacer(modifier = Modifier.height(20.dp))

                /* 🟪 Caja 2 – Texto historia (placeholder) */
                Text(
                    text = storySteps[currentStep].text, fontSize = 18.sp, color = GomiTextPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))
                DividerGomi()
                Spacer(modifier = Modifier.height(20.dp))

                /* 🟪 Caja 3 – Botón + progreso */
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(
                        enabled = !isLoading,
                        onClick = {
                            showDialog = true
                            isLoading = true
                            isCorrect = null

                            val color = colorCommands[currentStep]
                            onActivatePieces(color)
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Activar piezas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    ProgressBarPlaceholder(completedSteps)
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

        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Cargando...",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = GomiTextPrimary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    when {
                        isLoading -> {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.ic_loading),
                                contentDescription = "Cargando",
                                modifier = Modifier.size(64.dp)
                            )
                        }

                        isCorrect == true -> {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = "Correcto",
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = {
                                showDialog = false
                                isCorrect = null
                                isLoading = false

                                if (currentStep < storySteps.lastIndex) {
                                    currentStep++
                                } else {
                                    hasFinishedStory = true
                                    onStoryCompleted(
                                        "Historia Colores", errorCount, elapsedTime
                                    )
                                }
                            }) {
                                Text("Continuar")
                            }

                        }

                        else -> {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = "Error",
                                modifier = Modifier.size(64.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Errores: $errorCount", fontSize = 16.sp, color = Color.Red
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { showDialog = false }) {
                                Text("Intentar de nuevo")
                            }
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun DividerGomi() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(GomiBackground)
    )
}

@Composable
fun ProgressBarPlaceholder(currentStep: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (index < currentStep) Color(0xFF4CAF50) // GomiGreen
                        else GomiGreenLight, RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}
