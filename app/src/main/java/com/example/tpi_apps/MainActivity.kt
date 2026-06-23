package com.example.tpi_apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.ui.components.BottomNavigationBar
import com.example.tpi_apps.data.local.AppDatabase
import com.example.tpi_apps.data.repository.ReviewRepository
import com.example.tpi_apps.logic.ReviewViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpi_apps.ui.navigation.AppNavigation
import com.example.tpi_apps.ui.navigation.Routes
import com.example.tpi_apps.ui.theme.TPIappsTheme
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val reviewRepository = ReviewRepository.getInstance(database.reviewDao())

        enableEdgeToEdge()
        
        setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val viewModel: ReviewViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return ReviewViewModel(reviewRepository) as T
                    }
                }
            )
            val prefs = remember { context.getSharedPreferences("settings", android.content.Context.MODE_PRIVATE) }
            
            var darkTheme by remember { 
                mutableStateOf(prefs.getBoolean("dark_mode", false)) 
            }

            TPIappsTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()

                val sessionStatus by SupabaseModule.client.auth.sessionStatus.collectAsStateWithLifecycle()
                val session = (sessionStatus as? SessionStatus.Authenticated)?.session
                
                var currentUser by remember { mutableStateOf<User?>(null) }

                LaunchedEffect(session) {
                    if (session != null) {
                        try {
                            val userId = session.user?.id ?: ""
                            val profiles = SupabaseModule.apiService.getProfile("eq.$userId")
                            if (profiles.isNotEmpty()) {
                                currentUser = profiles[0]
                            } else {
                                val metaName = session.user?.userMetadata?.get("name")?.toString()?.replace("\"", "")
                                val metaUser = session.user?.userMetadata?.get("username")?.toString()?.replace("\"", "")
                                
                                val emailPrefix = session.user?.email?.split("@")?.get(0) ?: "usuario"
                                
                                val newUser = User(
                                    id = userId,
                                    name = metaName ?: emailPrefix.replaceFirstChar { it.uppercase() },
                                    username = metaUser ?: emailPrefix,
                                    email = session.user?.email ?: "",
                                    avatarSeed = "hamburger"
                                )
                                try {
                                    SupabaseModule.apiService.createProfile(newUser)
                                    currentUser = newUser
                                } catch (dbError: Exception) {
                                    dbError.printStackTrace()
                                    currentUser = newUser
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        currentUser = null
                    }
                }

                LaunchedEffect(sessionStatus) {
                    if (sessionStatus is SessionStatus.NotAuthenticated) {
                        currentUser = null
                        navController.navigate(Routes.Onboarding.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                val user = currentUser ?: User(
                    id = session?.user?.id,
                    name = "Cargando...",
                    username = "...",
                    email = session?.user?.email ?: "...",
                    avatarSeed = "hamburger",
                    points = 0,
                    level = "Bronce",
                    reputation = 0.0,
                    reviewCount = 0
                )

                val isSessionReady = sessionStatus !is SessionStatus.Initializing
                val hasAcceptedTerms = remember { prefs.getBoolean("terms_accepted", false) }
                
                val startDest = remember(isSessionReady, hasAcceptedTerms) {
                    if (isSessionReady) {
                        if (session != null) {
                            Routes.Inicio.route 
                        } else {
                            if (hasAcceptedTerms) Routes.Login.route else Routes.Onboarding.route
                        }
                    } else {
                        null
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (startDest == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        val showBottomBar = currentRoute != null && 
                                          currentRoute != Routes.Onboarding.route && 
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
                                onToggleDarkTheme = { 
                                    darkTheme = it
                                    prefs.edit().putBoolean("dark_mode", it).apply()
                                },
                                onUserUpdated = { updatedUser ->
                                    currentUser = updatedUser
                                },
                                modifier = Modifier.padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp),
                                startDestination = startDest,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
