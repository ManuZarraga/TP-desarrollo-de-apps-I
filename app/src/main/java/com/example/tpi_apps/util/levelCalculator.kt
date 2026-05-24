package com.example.tpi_apps.util

object LevelCalculator {
    const val SILVER_THRESHOLD = 250
    const val GOLD_THRESHOLD = 500
    const val SUPER_THRESHOLD = 800

    /**
     * Evalúa los puntos acumulados y devuelve la etiqueta de nivel correspondiente.
     */
    fun determineLevel(points: Int): String {
        return when {
            points >= SUPER_THRESHOLD -> "Súper Gourmet"
            points >= GOLD_THRESHOLD -> "Crítico de Oro"
            points >= SILVER_THRESHOLD -> "Crítico de Plata"
            else -> "Crítico de Bronce"
        }
    }
}