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
import com.espol.gummyapp.ui.screens.home.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiGreenLight
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch

@Composable
fun StoryColorsScreen(
    isBleConnected: Boolean,
    onActivatePieces: () -> Unit,
    onHomeClick: () -> Unit,
    onRecordClick: () -> Unit,
    onConnectionClick: () -> Unit,
    onCreditsClick: () -> Unit,
    onCloseApp: () -> Unit,
    onBackClick: () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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

                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "Menú",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                scope.launch { drawerState.open() }
                            })
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
                        text = "Aquí irá apareciendo el texto de la historia conforme avance el juego.",
                        fontSize = 18.sp,
                        color = GomiTextPrimary
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
                            onClick = onActivatePieces,
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

                        ProgressBarPlaceholder()
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
                    icon = R.drawable.ic_history,
                    label = "Historial",
                    onClick = onRecordClick
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
private fun DividerGomi() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(GomiBackground)
    )
}

@Composable
private fun ProgressBarPlaceholder() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(6) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(GomiGreenLight, RoundedCornerShape(4.dp))
            )
        }
    }
}
