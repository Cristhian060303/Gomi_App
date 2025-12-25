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
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.delay

@Composable
fun StoryFormsScreen(
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
            delay(1000)
            elapsedTime++
        }
    }

    LaunchedEffect(bleResponse) {
        if (!showDialog || !isLoading || bleResponse == null) return@LaunchedEffect

        when (bleResponse) {
            "CORRECTO" -> {
                isLoading = false
                isCorrect = true
                completedSteps++
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {

                Text(
                    text = "Formas",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = GomiTextPrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(20.dp))
                DividerGomi()
                Spacer(Modifier.height(20.dp))

                Text(
                    text = storyFormsSteps[currentStep].text,
                    fontSize = 18.sp,
                    color = GomiTextPrimary
                )

                Spacer(Modifier.height(20.dp))
                DividerGomi()
                Spacer(Modifier.height(20.dp))

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
                            onActivatePieces(
                                storyFormsSteps[currentStep].colorToSend
                            )
                        },
                        modifier = Modifier
                            .width(240.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Activar piezas", fontSize = 20.sp)
                    }

                    Spacer(Modifier.height(24.dp))
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

            BottomItem(R.drawable.ic_home, "Inicio", onHomeClick)
            BottomItem(R.drawable.ic_history, "Historial", onRecordClick)

            Box {
                BottomItem(R.drawable.ic_bluetooth, "Conexión", onConnectionClick)
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
            StoryResultDialog(
                isLoading = isLoading,
                isCorrect = isCorrect,
                errorCount = errorCount,
                onRetry = { showDialog = false },
                onContinue = {
                    showDialog = false
                    isCorrect = null
                    isLoading = false

                    if (currentStep < storyFormsSteps.lastIndex) {
                        currentStep++
                    } else {
                        hasFinishedStory = true
                        onStoryCompleted(
                            "Historia Formas", errorCount, elapsedTime
                        )
                    }
                })
        }
    }
}
