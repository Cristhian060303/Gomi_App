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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.espol.gummyapp.R
import com.espol.gummyapp.ui.components.SideMenuContent
import com.espol.gummyapp.ui.screens.home.BottomItem
import com.espol.gummyapp.ui.theme.GomiBackground
import com.espol.gummyapp.ui.theme.GomiTextPrimary
import kotlinx.coroutines.launch

@Composable
fun StorySelectionScreen(
    isBleConnected: Boolean,
    onColorsClick: () -> Unit,
    onFormsClick: () -> Unit,
    onCombinedClick: () -> Unit,
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

            Column(modifier = Modifier.fillMaxSize()) {

                // 🔝 Barra superior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Volver",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onBackClick() })

                    Image(
                        painter = painterResource(id = R.drawable.ic_more_vert),
                        contentDescription = "Menú",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                scope.launch { drawerState.open() }
                            })
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .background(Color.White, RoundedCornerShape(20.dp))
                ) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Text(
                                text = "Historias",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = GomiTextPrimary,
                                modifier = Modifier.padding(bottom = 32.dp)
                            )
                        }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.story_colors),
                                contentDescription = "Colores",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clickable { onColorsClick() },
                                contentScale = ContentScale.Fit
                            )
                        }

                        item { Spacer(modifier = Modifier.height(24.dp)) }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.story_forms),
                                contentDescription = "Formas",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clickable { onFormsClick() },
                                contentScale = ContentScale.Fit
                            )
                        }

                        item { Spacer(modifier = Modifier.height(24.dp)) }

                        item {
                            Image(
                                painter = painterResource(id = R.drawable.story_combined),
                                contentDescription = "Combinado",
                                modifier = Modifier
                                    .size(150.dp)
                                    .clickable { onCombinedClick() },
                                contentScale = ContentScale.Fit
                            )
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
    }
}

@Composable
fun BottomItem(
    icon: Int, label: String, onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp)) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label, fontSize = 12.sp, color = GomiTextPrimary
        )
    }
}