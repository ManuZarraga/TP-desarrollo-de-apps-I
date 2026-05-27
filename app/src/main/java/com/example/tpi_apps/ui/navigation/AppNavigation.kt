package com.example.tpi_apps.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.ui.screens.HomeScreen
import com.example.tpi_apps.ui.screens.ProfileScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.example.tpi_apps.ui.screens.CrearReseniaScreen
import com.example.tpi_apps.ui.screens.ExplorarScreen
import com.example.tpi_apps.ui.screens.BrandItemsScreen
import com.example.tpi_apps.ui.screens.ReseniaScreen

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
        composable(
            Routes.Inicio.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            HomeScreen(
                user = user,
                navController = navController,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ }
            )
        }
        composable(
            Routes.Resenia.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            ReseniaScreen()
        }
        composable(
            Routes.Camara.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            CrearReseniaScreen(
                user = user,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ }
            )
        }
        composable(
            Routes.Explorar.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            ExplorarScreen(navController = navController)
        }
        composable(
            Routes.BrandItems.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
            BrandItemsScreen(brandName = brandName)
        }
        composable(
            Routes.Perfil.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            ProfileScreen(
                user = user,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ }
            )
        }
    }
}