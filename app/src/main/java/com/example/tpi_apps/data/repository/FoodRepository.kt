package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepository {
    fun getFoods(): Flow<List<Food>> = flow {
        val foods = listOf(
            Food("1", "Cuarto de Libra", "McDonald's", "Hamburguesas", 1100.0, 520, 4.5, 120, "Clásica hamburguesa", "url"),
            Food("2", "Stacker XL", "Burger King", "Hamburguesas", 1500.0, 900, 4.8, 85, "Para los amantes del queso", "url"),
            Food("3", "Pizza Pepperoni", "Kentucky", "Pizza", 2200.0, 300, 4.2, 210, "Mucha muzzarella", "url"),
            Food("4", "Ensalada Caesar", "Green Eat", "Ensaladas", 1800.0, 350, 4.0, 45, "Fresca y natural", "url"),
            Food("5", "Sushi Roll", "Sushi Club", "Sushi", 3500.0, 400, 4.9, 150, "Salmón fresco", "url")
        )
        emit(foods)
    }
}
