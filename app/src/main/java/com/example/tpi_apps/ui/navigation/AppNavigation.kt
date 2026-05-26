package com.example.tpi_apps.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.ui.screens.HomeScreen
import com.example.tpi_apps.ui.screens.ProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    user: User,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Inicio.route,
        modifier = modifier
    ) {
        composable(Routes.Inicio.route) {
            HomeScreen(
                user = user,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ }
            )
        }
        composable(Routes.Resenia.route) {

        }
        composable(Routes.Camara.route) {

        }
        composable(Routes.Explorar.route) {

        }
        composable(Routes.Perfil.route) {
            ProfileScreen(
                user = user,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ }
            )
        }
    }
}