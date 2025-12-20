package com.espol.gummyapp.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.espol.gummyapp.R
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onContinue()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onContinue() }) {
        Image(
            painter = painterResource(id = R.drawable.portada_gomi),
            contentDescription = "Portada Gomi App",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
