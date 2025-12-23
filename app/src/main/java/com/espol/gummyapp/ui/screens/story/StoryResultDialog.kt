package com.espol.gummyapp.ui.screens.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.espol.gummyapp.ui.theme.GomiTextPrimary

@Composable
fun StoryResultDialog(
    isLoading: Boolean,
    isCorrect: Boolean?,
    errorCount: Int,
    onRetry: () -> Unit,
    onContinue: () -> Unit
) {
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
                .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Cargando...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GomiTextPrimary
            )

            Spacer(Modifier.height(24.dp))

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

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = onContinue) {
                        Text("Continuar")
                    }
                }

                else -> {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = "Error",
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Errores: $errorCount", fontSize = 16.sp, color = Color.Red
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = onRetry) {
                        Text("Intentar de nuevo")
                    }
                }
            }
        }
    }
}
