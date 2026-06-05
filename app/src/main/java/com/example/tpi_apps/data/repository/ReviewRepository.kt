package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReviewRepository {
    private val _reviews = MutableStateFlow(listOf(
        Review(
            id = "1",
            restaurantName = "Kentucky", 
            itemName = "Pizza Margarita", 
            rating = 5, 
            foodCategory = "Pizzas", 
            itemPrice = 1400.0, 
            comment = "Llegó en excelente estado, nada corrido y...",
            date = "2024-04-13",
            time = "12:30",
            likes = 150,
            username = "Manu Zarraga"
        ),
        Review(
            id = "2",
            restaurantName = "McDonald's", 
            itemName = "Big Mac", 
            rating = 2, 
            foodCategory = "Hamburguesas", 
            itemPrice = 1200.0, 
            comment = "Los medallones son chicos y trae poca...",
            date = "2024-04-12",
            time = "14:15",
            likes = 80,
            username = "Milagro Gonzales Nuñez"
        ),
        Review(
            id = "3",
            restaurantName = "La Juvenil", 
            itemName = "Capelletis al Pesto", 
            rating = 4, 
            foodCategory = "Pastas", 
            itemPrice = 1300.0, 
            comment = "No vino con tantas almendras, pero muy...",
            date = "2024-04-11",
            time = "21:00",
            likes = 210,
            username = "Federico Dip"
        ),
        Review(
            id = "4",
            restaurantName = "Burger King", 
            itemName = "Stacker XL", 
            rating = 5, 
            foodCategory = "Hamburguesas", 
            itemPrice = 1500.0, 
            comment = "Increíble sabor, la carne muy jugosa.",
            date = "2024-04-10",
            time = "20:45",
            likes = 300,
            username = "Federico Dip"
        )
    ))

    private val _likedReviewIds = MutableStateFlow<Set<String>>(emptySet())
    val likedReviewIds: StateFlow<Set<String>> = _likedReviewIds.asStateFlow()

    fun getReviews(): Flow<List<Review>> = _reviews.asStateFlow()

    fun addReview(review: Review) {
        _reviews.update { currentReviews ->
            listOf(review) + currentReviews
        }
    }

    fun toggleLike(reviewId: String) {
        val currentLiked = _likedReviewIds.value
        val isLiked = currentLiked.contains(reviewId)
        
        if (isLiked) {
            _likedReviewIds.value = currentLiked - reviewId
            updateLikes(reviewId, false)
        } else {
            _likedReviewIds.value = currentLiked + reviewId
            updateLikes(reviewId, true)
        }
    }

    private fun updateLikes(reviewId: String, increment: Boolean) {
        _reviews.update { currentReviews ->
            currentReviews.map {
                if (it.id == reviewId) {
                    val newLikes = if (increment) it.likes + 1 else (it.likes - 1).coerceAtLeast(0)
                    it.copy(likes = newLikes)
                } else {
                    it
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ReviewRepository? = null

        fun getInstance(): ReviewRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ReviewRepository().also { INSTANCE = it }
            }
        }
    }
}
