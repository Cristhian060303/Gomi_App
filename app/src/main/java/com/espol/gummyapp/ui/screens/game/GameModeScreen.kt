package com.espol.gummyapp.ui.screens.game

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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.components.SideMenuContent
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch

@Composable
fun GameModeScreen(
    onHistoryClick: () -> Unit,
    onMemoryClick: () -> Unit,
    onFreeClick: () -> Unit,
    onHomeClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseApp: () -> Unit,
    onBackClick: () -> Unit,
    onRecordClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            SideMenuContent(
                onHelpClick = { /* luego */ },
                onCreditsClick = onCreditsClick,
                onCloseClick = onCloseApp
            )
        }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(GomiBackground)
        ) {

            // 🔝 Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onBackClick() })

                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Menú",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            scope.launch { drawerState.open() }
                        })
            }

            // 🎮 Contenido central
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ModeButton("Historia", onHistoryClick)
                Spacer(modifier = Modifier.height(24.dp))
                ModeButton("Memoria", onMemoryClick)
                Spacer(modifier = Modifier.height(24.dp))
                ModeButton("Libre", onFreeClick)
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
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
}

@Composable
private fun ModeButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(240.dp)
            .height(60.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = text, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun BottomItem(
    icon: Int, label: String, onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp)) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp, color = GomiTextPrimary)
    }
}
