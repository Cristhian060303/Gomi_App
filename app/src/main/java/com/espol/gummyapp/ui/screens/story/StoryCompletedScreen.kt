package com.espol.gummyapp.ui.screens.story

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun StoryCompletedScreen(
    modeName: String,
    totalErrors: Int,
    totalTimeSeconds: Int,
    onHomeClick: () -> Unit,
    onRecordClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onBackClick: () -> Unit,
    onInterrupt: () -> Unit
) {
    LaunchedEffect(Unit) {
        onInterrupt()
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
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "¡Bien hecho!",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = GomiTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Has completado el juego.", fontSize = 18.sp, color = GomiTextPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_gomi_happy),
                    contentDescription = "Gomi feliz",
                    modifier = Modifier.size(180.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Errores totales  $totalErrors",
                    fontSize = 18.sp,
                    color = GomiTextPrimary
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onHomeClick,
                    modifier = Modifier
                        .width(220.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Volver al inicio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
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

            BottomItem(
                icon = R.drawable.ic_bluetooth, label = "Conexión", onClick = onConnectionClick
            )
        }
    }
}