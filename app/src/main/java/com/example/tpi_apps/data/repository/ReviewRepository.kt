package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReviewRepository {
    fun getReviews(): Flow<List<Review>> = flow {
        val reviews = listOf(
            Review(restaurantName = "McDonald's", itemName = "Big Mac", rating = 4, foodCategory = "Hamburguesas", itemPrice = 1200.0, comment = "Muy bueno"),
            Review(restaurantName = "Burger King", itemName = "Whopper", rating = 5, foodCategory = "Hamburguesas", itemPrice = 1300.0, comment = "Excelente"),
            Review(restaurantName = "Mostaza", itemName = "Mega Bacon", rating = 3, foodCategory = "Hamburguesas", itemPrice = 1400.0, comment = "Puede mejorar")
        )
        emit(reviews)
    }
}
