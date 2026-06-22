package com.example.tpi_apps.data.model
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class User(
    val id: String? = null,
    var name: String = "",
    val username: String = "",
    var email: String = "",
    @kotlinx.serialization.SerialName("avatar_seed") val avatarSeed: String = "default",
    var points: Int = 0,
    var level: String = "Bronce",
    var reputation: Double = 0.0,
    @kotlinx.serialization.SerialName("review_count") var reviewCount: Int = 0,
    @kotlinx.serialization.SerialName("joined_date") val joinedDate: String = java.time.LocalDate.now().toString(),
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