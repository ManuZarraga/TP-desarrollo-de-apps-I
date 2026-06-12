package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val id: String,
    val name: String,
    val restaurant: String,
    val category: String,
    val price: Double,
    val calories: Int? = null,
    val rating: Double,
    @SerialName("review_count")
    val reviewCount: Int,
    val description: String,
    @SerialName("image_url")
    val imageUrl: String
)
