package com.example.tpi_apps.data.network

import com.example.tpi_apps.data.dto.ProfileUpdateRequest
import com.example.tpi_apps.data.dto.ReviewCreateRequest
import com.example.tpi_apps.data.dto.ReviewLikeRequest
import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.model.User
import kotlinx.serialization.Serializable
import retrofit2.http.*

interface SupabaseApiService {

    @GET("profiles")
    suspend fun getProfile(
        @Query("id") id: String,
        @Query("select") select: String = "*"
    ): List<User>

    @GET("profiles")
    suspend fun getProfileByUsername(
        @Query("username") username: String,
        @Query("select") select: String = "id"
    ): List<User>

    @GET("profiles")
    suspend fun getProfileByEmail(
        @Query("email") email: String,
        @Query("select") select: String = "id"
    ): List<User>

    @POST("profiles")
    suspend fun createProfile(
        @Body user: User,
        @Header("Prefer") prefer: String = "return=minimal"
    )

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
        @Query("id") id: String? = null,
        @Query("select") select: String = "*,profiles(username,level,avatar_seed),brands(name),foods(name,category,price,description,image_url)"
    ): List<Review>

    @POST("reviews")
    suspend fun addReview(
        @Body review: ReviewCreateRequest,
        @Header("Prefer") prefer: String = "return=minimal"
    )

    @GET("review_likes")
    suspend fun getLikes(
        @Query("user_id") userId: String
    ): List<com.example.tpi_apps.data.model.ReviewLike>

    @POST("review_likes")
    suspend fun addLike(
        @Body request: ReviewLikeRequest,
        @Header("Prefer") prefer: String = "return=minimal"
    )

    @DELETE("review_likes")
    suspend fun removeLike(
        @Query("review_id") reviewId: String,
        @Query("user_id") userId: String
    )

    @PATCH("profiles")
    suspend fun updateProfile(
        @QueryMap filters: Map<String, String>,
        @Body updates: ProfileUpdateRequest,
        @Header("Prefer") prefer: String = "return=minimal"
    )

    @PATCH("reviews")
    suspend fun updateReviewLikes(
        @Query("id") id: String,
        @Body updates: Map<String, Int>,
        @Header("Prefer") prefer: String = "return=minimal"
    )
}
