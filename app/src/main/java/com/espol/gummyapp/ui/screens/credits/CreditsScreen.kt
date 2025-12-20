package com.espol.gummyapp.ui.screens.credits

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.espol.gummyapp.R

@Composable
fun CreditsScreen(
    onDismiss: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.creditos_gomi),
        contentDescription = "Créditos",
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() },
        contentScale = ContentScale.Crop
    )
}
