package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.dto.ReviewCreateRequest
import com.example.tpi_apps.data.dto.ReviewLikeRequest
import com.example.tpi_apps.data.local.dao.ReviewDao
import com.example.tpi_apps.data.local.entities.ReviewEntity
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.model.ReviewLike
import com.example.tpi_apps.data.network.SupabaseModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ReviewRepository(private val reviewDao: ReviewDao? = null) {
    private val _likedReviewIds = MutableStateFlow<Set<String>>(emptySet())
    val likedReviewIds: StateFlow<Set<String>> = _likedReviewIds.asStateFlow()

    fun getReviews(): Flow<List<Review>> = flow {
        reviewDao?.getAllReviews()?.map { entities ->
            entities.map { it.toDomain() }
        }?.first()?.let { localReviews ->
            if (localReviews.isNotEmpty()) emit(localReviews)
        }

        try {
            val remoteReviews = SupabaseModule.apiService.getReviews()

            reviewDao?.insertReviews(remoteReviews.map { it.toEntity() })

            emit(remoteReviews)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getUserReviews(userId: String): Flow<List<Review>> {
        return reviewDao?.getReviewsByUser(userId)?.map { entities ->
            entities.map { it.toDomain() }
        } ?: flow { emit(emptyList()) }
    }

    suspend fun getReviewById(id: String): Review? {
        return reviewDao?.getReviewById(id)?.toDomain()
    }

    suspend fun addReview(review: Review): Boolean {
        return try {
            val request = ReviewCreateRequest(
                id = review.id,
                userId = review.userId ?: "",
                brandId = review.brandId ?: "",
                foodId = review.foodId ?: "",
                rating = review.rating,
                comment = review.comment,
                images = review.images,
                date = review.date,
                time = review.time,
                likes = review.likes
            )
            SupabaseModule.apiService.addReview(request)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun loadUserLikes(userId: String) {
        try {
            val likes = SupabaseModule.apiService.getLikes("eq.$userId")
            _likedReviewIds.value = likes.map { it.reviewId }.toSet()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun toggleLike(reviewId: String, userId: String): Boolean {
        val currentLiked = _likedReviewIds.value
        val isLiked = currentLiked.contains(reviewId)

        _likedReviewIds.value = if (isLiked) currentLiked - reviewId else currentLiked + reviewId
        
        return try {
            if (isLiked) {
                // Caso: Quitar Like
                SupabaseModule.apiService.removeLike("eq.$reviewId", "eq.$userId")
                // 'likes' en la tabla 'reviews' se actualizará vía Trigger en Supabase
            } else {
                // Caso: Dar Like
                SupabaseModule.apiService.addLike(ReviewLikeRequest(reviewId = reviewId, userId = userId))
                // 'likes' en la tabla 'reviews' se actualizará vía Trigger en Supabase
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            _likedReviewIds.value = currentLiked
            false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ReviewRepository? = null

        fun getInstance(reviewDao: ReviewDao? = null): ReviewRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ReviewRepository(reviewDao).also { INSTANCE = it }
            }
        }
    }
}

// Mappers
fun Review.toEntity(): ReviewEntity = ReviewEntity(
    id = id,
    userId = userId,
    brandId = brandId,
    foodId = foodId,
    rating = rating,
    comment = comment,
    images = images?.joinToString(","),
    date = date,
    time = time,
    likes = likes,
    username = username,
    restaurantName = restaurantName,
    itemName = itemName,
    foodCategory = foodCategory,
    itemPrice = itemPrice,
    itemDescription = itemDescription
)

fun ReviewEntity.toDomain(): Review = Review(
    id = id,
    userId = userId,
    brandId = brandId,
    foodId = foodId,
    rating = rating,
    comment = comment,
    images = images?.split(",")?.filter { it.isNotEmpty() },
    date = date,
    time = time,
    likes = likes
)
