package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandName(val name: String)

@Serializable
data class Food(
    val id: String? = null,
    val name: String = "",
    @SerialName("brand_id")
    val brandId: String? = null,
    val category: String = "",
    val price: Double = 0.0,
    val calories: Int? = 0,
    val rating: Double = 0.0,
    @SerialName("review_count")
    val reviewCount: Int = 0,
    val description: String? = "",
    @SerialName("image_url")
    val imageUrl: String? = "",
    @SerialName("brands")
    val brands: BrandName? = null
) {
    val restaurant: String get() = brands?.name ?: "Marca"
}
