package com.example.tpi_apps.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tpi_apps.ui.navigation.BottomNavItem
import com.example.tpi_apps.ui.theme.AppBlue
import com.example.tpi_apps.ui.theme.AppGrey

@Composable
fun BottomNavigationBar(navController: NavController) {
    val iconSize = 30.dp
    val perfilIconSize = 25.dp
    val cameraIconSize = 40.dp
    val items = listOf(
        BottomNavItem.Inicio,
        BottomNavItem.Resenia,
        BottomNavItem.Camara,
        BottomNavItem.Explorar,
        BottomNavItem.Perfil
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route
                    
                    if (item == BottomNavItem.Camara) {
                        Spacer(modifier = Modifier.size(64.dp))
                    } else {
                        IconButton(
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = false
                                        }
                                        launchSingleTop = true
                                        restoreState = false
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label,
                                tint = if (isSelected) AppBlue else AppGrey,
                                modifier = Modifier.size(
                                    if (item == BottomNavItem.Perfil) perfilIconSize else iconSize
                                )
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(y = (-15.dp))
                .size(64.dp)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(AppBlue)
                .clickable {
                    val camaraRoute = BottomNavItem.Camara.route
                    if (currentRoute != camaraRoute) {
                        navController.navigate(camaraRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = BottomNavItem.Camara.icon),
                contentDescription = BottomNavItem.Camara.label,
                tint = Color.White,
                modifier = Modifier.size(cameraIconSize)
            )
        }
    }
}