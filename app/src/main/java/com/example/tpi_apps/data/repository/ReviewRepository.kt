package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReviewRepository {
    fun getReviews(): Flow<List<Review>> = flow {
        val reviews = listOf(
            Review(restaurantName = "Kentucky", itemName = "Pizza Margarita", rating = 5, foodCategory = "Pizzas", itemPrice = 1400.0, comment = "“Llegó en excelente estado, nada corrido y..."),
            Review(restaurantName = "McDonald's", itemName = "Big Mac", rating = 2, foodCategory = "Hamburguesas", itemPrice = 1200.0, comment = "“Los medallones son chicos y trae poca..."),
            Review(restaurantName = "La Juvenil", itemName = "Capelletis al Pesto", rating = 4, foodCategory = "Pastas", itemPrice = 1300.0, comment = "“No vino con tantas almendras, pero muy..."),
        )
        emit(reviews)
    }
}
