package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewLike(
    val id: String? = null,
    @SerialName("review_id")
    val reviewId: String,
    @SerialName("user_id")
    val userId: String
)
