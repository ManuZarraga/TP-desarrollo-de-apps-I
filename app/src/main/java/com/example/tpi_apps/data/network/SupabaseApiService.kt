package com.example.tpi_apps.data.network

import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import kotlinx.serialization.Serializable
import retrofit2.http.*

@Serializable
data class ReviewLikeRequest(
    @kotlinx.serialization.SerialName("review_id") val reviewId: String,
    @kotlinx.serialization.SerialName("user_id") val userId: String
)

interface SupabaseApiService {

    @GET("brands")
    suspend fun getBrands(
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): List<Brand>

    @GET("foods")
    suspend fun getFoods(
        @Query("select") select: String = "*,brands(name)"
    ): List<Food>

    @GET("reviews")
    suspend fun getReviews(
        @Query("select") select: String = "*,profiles(username),brands(name),foods(name,category,price)"
    ): List<Review>

    @POST("reviews")
    suspend fun addReview(
        @Body review: Review,
        @Header("Prefer") prefer: String = "return=minimal"
    )

    @POST("review_likes")
    suspend fun addLike(
        @Body request: ReviewLikeRequest,
        @Header("Prefer") prefer: String = "return=representation"
    )

    @DELETE("review_likes")
    suspend fun removeLike(
        @Query("review_id") reviewId: String,
        @Query("user_id") userId: String
    )
}
