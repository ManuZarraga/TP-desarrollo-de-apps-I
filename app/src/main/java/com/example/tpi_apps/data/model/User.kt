package com.example.tpi_apps.data.model
import java.time.LocalDate

data class User(
    var name: String,
    val username: String,
    var email: String,
    val avatarSeed: String,
    var points: Int,
    var level: String,
    var reputation: Double,
    var reviewCount: Int,
    val joinedDate: String = LocalDate.now().toString(),
    val badges: List<String> = emptyList()
) {
    /**
     * Calcula los puntos restantes para alcanzar el rango Oro.
     */
    fun getPointsToNextLevel(nextLevelPoints: Int = 500): Int {
        return (nextLevelPoints - points).coerceAtLeast(0)
    }

    /**
     * Obtiene el porcentaje de progreso (0.0 a 1.0) para el avance de nivel.
     */
    fun getProgressPercent(nextLevelPoints: Int = 500): Float {
        if (points >= nextLevelPoints) return 1.0f
        return points.toFloat() / nextLevelPoints.toFloat()
    }
}