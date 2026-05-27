package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReviewRepository {
    fun getReviews(): Flow<List<Review>> = flow {
        val reviews = listOf(
            Review(
                restaurantName = "Kentucky", 
                itemName = "Pizza Margarita", 
                rating = 5, 
                foodCategory = "Pizzas", 
                itemPrice = 1400.0, 
                comment = "Llegó en excelente estado, nada corrido y...",
                date = "2024-04-13",
                likes = 150,
                username = "Manu Zarraga"
            ),
            Review(
                restaurantName = "McDonald's", 
                itemName = "Big Mac", 
                rating = 2, 
                foodCategory = "Hamburguesas", 
                itemPrice = 1200.0, 
                comment = "Los medallones son chicos y trae poca...",
                date = "2024-04-12",
                likes = 80,
                username = "Milagro Gonzales Nuñez"
            ),
            Review(
                restaurantName = "La Juvenil", 
                itemName = "Capelletis al Pesto", 
                rating = 4, 
                foodCategory = "Pastas", 
                itemPrice = 1300.0, 
                comment = "No vino con tantas almendras, pero muy...",
                date = "2024-04-11",
                likes = 210,
                username = "Federico Dip"
            ),
            Review(
                restaurantName = "Burger King", 
                itemName = "Stacker XL", 
                rating = 5, 
                foodCategory = "Hamburguesas", 
                itemPrice = 1500.0, 
                comment = "Increíble sabor, la carne muy jugosa.",
                date = "2024-04-10",
                likes = 300,
                username = "Federico Dip"
            )
        )
        emit(reviews)
    }
}
