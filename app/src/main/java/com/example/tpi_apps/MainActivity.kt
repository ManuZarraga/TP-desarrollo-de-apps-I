package com.example.tpi_apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.ui.components.BottomNavigationBar
import com.example.tpi_apps.ui.navigation.AppNavigation
import com.example.tpi_apps.ui.navigation.Routes
import com.example.tpi_apps.ui.theme.TPIappsTheme
import com.example.tpi_apps.ui.theme.TPIappsTheme
import com.example.tpi_apps.util.LevelCalculator
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.auth.auth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            TPIappsTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                
                // Verificar si hay una sesión activa
                val session = SupabaseModule.client.auth.currentSessionOrNull()
                val startDest = if (session != null) Routes.Inicio.route else Routes.Onboarding.route

                var currentUser by remember {
                    mutableStateOf<User?>(null)
                }

                LaunchedEffect(session) {
                    if (session != null) {
                        try {
                            val profiles = SupabaseModule.apiService.getProfile(session.user?.id ?: "")
                            if (profiles.isNotEmpty()) {
                                currentUser = profiles[0]
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                val user = currentUser ?: User(
                    name = "Federico Dip",
                    username = "federicodip",
                    email = "federicodip20@gmail.com",
                    avatarSeed = "profile_main",
                    points = 360,
                    level = "Crítico de Plata",
                    reputation = 4.8,
                    reviewCount = 2
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val showBottomBar = currentRoute != Routes.Onboarding.route && 
                                      currentRoute != Routes.Login.route && 
                                      currentRoute != Routes.SignUp.route

                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.background,
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    ) { innerPadding ->
                        AppNavigation(
                            navController = navController,
                            user = user,
                            isDarkTheme = darkTheme,
                            onToggleDarkTheme = { darkTheme = it },
                            modifier = Modifier.padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp),
                            startDestination = startDest
                        )
                    }
                }
            }
        }
    }
}