package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(val username: String = "")

@Serializable
data class FoodData(
    val name: String = "",
    val category: String = "",
    val price: Double = 0.0
)

@Serializable
data class Review(
    val id: String = "",
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("brand_id")
    val brandId: String? = null,
    @SerialName("food_id")
    val foodId: String? = null,
    val rating: Int = 0,
    val comment: String? = "",
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("review_date")
    val date: String? = "",
    @SerialName("review_time")
    val time: String? = "",
    @SerialName("likes")
    var likes: Int = 0,
    
    @SerialName("profiles")
    val profiles: UserData? = null,
    @SerialName("brands")
    val brands: BrandName? = null,
    @SerialName("foods")
    val foods: FoodData? = null
) {
    val username: String get() = profiles?.username ?: "Usuario"
    val restaurantName: String get() = brands?.name ?: "Restaurante"
    val itemName: String get() = foods?.name ?: "Producto"
    val foodCategory: String get() = foods?.category ?: ""
    val itemPrice: Double? get() = foods?.price
    val displayDate: String get() = date ?: ""
    val displayTime: String get() = time ?: ""
}
