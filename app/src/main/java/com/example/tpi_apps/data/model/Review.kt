package com.example.tpi_apps.data.model
import java.util.UUID
import java.time.LocalDate
import java.time.LocalTime



data class Review(
    val id: String = UUID.randomUUID().toString(),
    val restaurantName: String,
    val itemName: String,
    val rating: Int, // 1 a 5 estrellas
    val comment: String,
    val imageUrl: String? = null,
    val date: String = LocalDate.now().toString(),
    val time: String = LocalTime.now().toString().substring(0, 5),
    val foodCategory: String,
    val itemPrice: Double? = null,
    var likes: Int = 0,
    var username: String
) {
    fun incrementLikes() {
        likes += 1
    }
}