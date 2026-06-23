package com.example.tpi_apps.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val userId: String?,
    val brandId: String?,
    val foodId: String?,
    val rating: Int,
    val comment: String?,
    val images: String?,
    val date: String?,
    val time: String?,
    val likes: Int,
    val username: String?,
    val restaurantName: String?,
    val itemName: String?,
    val foodCategory: String?,
    val itemPrice: Double?,
    val itemDescription: String?
)
