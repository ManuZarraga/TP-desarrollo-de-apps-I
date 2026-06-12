package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    @SerialName("restaurant_name")
    val restaurantName: String,
    @SerialName("item_name")
    val itemName: String,
    val rating: Int,
    val comment: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val date: String,
    val time: String,
    @SerialName("food_category")
    val foodCategory: String,
    @SerialName("item_price")
    val itemPrice: Double? = null,
    @SerialName("likes_count")
    var likes: Int = 0,
    val username: String
) {
    fun incrementLikes() {
        likes += 1
    }
}
