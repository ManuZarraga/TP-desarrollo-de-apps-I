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
import com.example.tpi_apps.ui.screens.ConfirmationScreen
import com.example.tpi_apps.ui.screens.ReseniaListScreen
import com.example.tpi_apps.ui.screens.ReseniaSpecificScreen
import com.example.tpi_apps.ui.screens.OnboardingScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    user: User,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Onboarding.route,
        modifier = modifier
    ) {
        composable(
            Routes.Onboarding.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.Inicio.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
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
            ReseniaScreen(
                onReviewClick = { reviewId ->
                    navController.navigate(Routes.ReseniaSpecific.createRoute(reviewId))
                }
            )
        }
        composable(
            Routes.Camara.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            CrearReseniaScreen(
                user = user,
                navController = navController,
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
            BrandItemsScreen(brandName = brandName, navController = navController)
        }
        composable(
            Routes.Perfil.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            ProfileScreen(
                user = user,
                navController = navController
            )
        }
        composable(
            Routes.Confirmacion.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            ConfirmationScreen(navController = navController)
        }
        composable(
            Routes.ReseniaList.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) { backStackEntry ->
            val brandName = backStackEntry.arguments?.getString("brandName") ?: ""
            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
            ReseniaListScreen(brandName = brandName, itemName = itemName, navController = navController)
        }
        composable(
            Routes.ReseniaSpecific.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getString("reviewId") ?: ""
            ReseniaSpecificScreen(reviewId = reviewId, navController = navController)
        }
    }
}