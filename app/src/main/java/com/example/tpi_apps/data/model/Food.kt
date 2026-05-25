package com.example.tpi_apps.data.model

data class Food(
    val id: String,
    val name: String,
    val restaurant: String,
    val category: String,
    val price: Double,
    val calories: Int? = null,
    val rating: Double,
    val reviewCount: Int,
    val description: String,
    val imageUrl: String
)