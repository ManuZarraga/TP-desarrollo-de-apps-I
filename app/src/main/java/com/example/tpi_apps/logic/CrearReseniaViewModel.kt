package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.data.repository.ReviewRepository
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class CrearReseniaViewModel(
    private val reviewRepository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel() {

    fun submitReview(
        user: User,
        restaurantName: String,
        itemName: String,
        rating: Int,
        comment: String
    ) {
        val newReview = Review(
            id = UUID.randomUUID().toString(),
            restaurantName = restaurantName,
            itemName = itemName,
            rating = rating,
            comment = comment,
            foodCategory = detectCategory(itemName),
            username = user.name,
            date = LocalDate.now().toString(),
            time = LocalTime.now().toString().substring(0, 5),
            likes = 0
        )
        
        reviewRepository.addReview(newReview)

        // Actualizar estadísticas del usuario
        user.points += 50
        user.reviewCount += 1
        // Cálculo simple de reputación: (reputación actual * reseñas anteriores + nueva puntuación) / reseñas totales
        val totalReviews = user.reviewCount.toDouble()
        user.reputation = ((user.reputation * (totalReviews - 1)) + rating) / totalReviews
    }

    private fun detectCategory(itemName: String): String {
        return when {
            itemName.contains("Pizza", ignoreCase = true) -> "Pizzas"
            itemName.contains("Burger", ignoreCase = true) || itemName.contains("Big Mac", ignoreCase = true) -> "Hamburguesas"
            itemName.contains("Sushi", ignoreCase = true) -> "Sushi"
            itemName.contains("Pasta", ignoreCase = true) -> "Pastas"
            itemName.contains("Shawarma", ignoreCase = true) -> "Shawarma"
            else -> "Postres"
        }
    }
}
