package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.network.ReviewLikeRequest
import com.example.tpi_apps.data.network.SupabaseModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class ReviewRepository {
    private val _likedReviewIds = MutableStateFlow<Set<String>>(emptySet())
    val likedReviewIds: StateFlow<Set<String>> = _likedReviewIds.asStateFlow()

    fun getReviews(): Flow<List<Review>> = flow {
        try {
            val reviews = SupabaseModule.apiService.getReviews()
            emit(reviews)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    suspend fun addReview(review: Review): Boolean {
        return try {
            // Retrofit enviará automáticamente el objeto JSON con brand_id, food_id, user_id, etc.
            SupabaseModule.apiService.addReview(review)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun toggleLike(reviewId: String, userId: String) {
        val currentLiked = _likedReviewIds.value
        val isLiked = currentLiked.contains(reviewId)
        
        try {
            if (isLiked) {
                SupabaseModule.apiService.removeLike(reviewId, userId)
                _likedReviewIds.value = currentLiked - reviewId
            } else {
                SupabaseModule.apiService.addLike(ReviewLikeRequest(reviewId, userId))
                _likedReviewIds.value = currentLiked + reviewId
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
