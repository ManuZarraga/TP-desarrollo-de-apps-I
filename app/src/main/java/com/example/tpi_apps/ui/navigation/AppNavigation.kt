package com.example.tpi_apps.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
import com.example.tpi_apps.ui.auth.LoginScreen
import com.example.tpi_apps.ui.auth.SignUpScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    user: User,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = Routes.Onboarding.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            Routes.Onboarding.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            Routes.Login.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Inicio.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SignUp.route)
                }
            )
        }
        composable(
            Routes.SignUp.route,
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
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
            arguments = listOf(navArgument("imageUri") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }),
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) { backStackEntry ->
            val imageUri = backStackEntry.arguments?.getString("imageUri")
            CrearReseniaScreen(
                user = user,
                navController = navController,
                onSettingsClick = { /* TODO */ },
                onReviewsClick = { /* TODO */ },
                initialImageUri = imageUri?.let { Uri.parse(it) }
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
                navController = navController,
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = onToggleDarkTheme
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