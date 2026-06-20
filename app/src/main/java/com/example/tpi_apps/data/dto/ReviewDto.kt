package com.example.tpi_apps.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewCreateRequest(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("brand_id") val brandId: String,
    @SerialName("food_id") val foodId: String,
    @SerialName("rating") val rating: Int,
    @SerialName("comment") val comment: String?,
    @SerialName("image_url") val imageUrl: String?,
    @SerialName("review_date") val date: String?,
    @SerialName("review_time") val time: String?,
    @SerialName("likes") val likes: Int = 0
)

@Serializable
data class ReviewLikeRequest(
    @SerialName("review_id") val reviewId: String,
    @SerialName("user_id") val userId: String
)
