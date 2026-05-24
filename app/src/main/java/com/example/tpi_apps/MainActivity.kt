package com.example.tpi_apps

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.ui.screens.ProfileScreen
import com.example.tpi_apps.util.LevelCalculator


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Estado mutable principal del usuario crítico
            var user by remember {
                mutableStateOf(
                    User(
                        name = "Federico Dip",
                        username = "federicodip",
                        email = "federicodip20@gmail.com",
                        avatarSeed = "profile_main",
                        points = 360,
                        level = "Crítico de Plata",
                        reputation = 4.8,
                        reviewCount = 2
                    )
                )
            }

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen(
                        user = user,
                        onSettingsClick = {
                            // Acción para modificar detalles
                            Toast.makeText(this@MainActivity, "Abrir Ajustes", Toast.LENGTH_SHORT).show()
                        },
                        onReviewsClick = {
                            // Navegación hacia reseñas históricas
                            Toast.makeText(this@MainActivity, "Mostrando opiniones", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

    /**
     * Incrementa la puntuación del comensal y recalcula su nivel.
     */
    private fun addExperiencePoints(user: User, gainedXP: Int): User {
        val nextPoints = user.points + gainedXP
        val nextLevel = LevelCalculator.determineLevel(nextPoints)

        return user.copy(
            points = nextPoints,
            level = nextLevel,
            reviewCount = user.reviewCount + 1
        )
    }
}