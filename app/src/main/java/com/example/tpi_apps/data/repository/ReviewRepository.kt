package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class ReviewRepository {
    private val _likedReviewIds = MutableStateFlow<Set<String>>(emptySet())
    val likedReviewIds: StateFlow<Set<String>> = _likedReviewIds.asStateFlow()

    fun getReviews(): Flow<List<Review>> = flow {
        try {
            val reviews = SupabaseModule.client.postgrest["reviews"]
                .select {
                    order(column = "created_at", order = Order.DESCENDING)
                }
                .decodeList<Review>()
            emit(reviews)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    fun toggleLike(reviewId: String) {
        val currentLiked = _likedReviewIds.value
        val isLiked = currentLiked.contains(reviewId)
        
        if (isLiked) {
            _likedReviewIds.value = currentLiked - reviewId
            // TODO: Persistir en Supabase (ej. RPC o trigger)
        } else {
            _likedReviewIds.value = currentLiked + reviewId
            // TODO: Persistir en Supabase (ej. RPC o trigger)
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
