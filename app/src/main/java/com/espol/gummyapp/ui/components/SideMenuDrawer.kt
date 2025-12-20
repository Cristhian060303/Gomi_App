package com.espol.gummyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiPrimary
import com.espol.gummyapp.ui.theme.GomiTextPrimary

@Composable
fun SideMenuContent(
    onHelpClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
            .background(GomiBackground)
            .padding(top = 40.dp, start = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        MenuItem(
            icon = R.drawable.ic_help,
            text = "Ayuda",
            onClick = onHelpClick
        )

        MenuItem(
            icon = R.drawable.ic_star,
            text = "Créditos",
            onClick = onCreditsClick
        )

        MenuItem(
            icon = R.drawable.ic_close,
            text = "Cerrar",
            onClick = onCloseClick
        )
    }
}

@Composable
private fun MenuItem(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = GomiTextPrimary,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = GomiTextPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
