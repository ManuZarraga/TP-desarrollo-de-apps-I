package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepository {
    fun getFoods(): Flow<List<Food>> = flow {
        val foods = listOf(
            Food("1", "Cuarto de Libra", "McDonald's", "Hamburguesas", 12000.0, 520, 4.5, 120, "Clásica hamburguesa con carne de res, queso, lechuga y tomate", "url"),
            Food("2", "Stacker XL", "Burger King", "Hamburguesas", 15000.0, 900, 4.8, 85, "Para los amantes del queso y la carne", "url"),
            Food("3", "Pizza Pepperoni", "Kentucky", "Pizza", 16500.0, 300, 4.2, 210, "Mucha muzzarella y la salsa es una mezcla de especias muy ricas", "url"),
            Food("4", "Wrap de Pollo", "Shami Shawarma", "Shawarma", 14000.0, 350, 4.0, 67, "Deliciso wrap con salsa spicy", "url"),
            Food("5", "Tallarines a la Pomarola", "La Juvenil", "Pastas", 13500.0, 350, 4.0, 29, "Pasta fresca y salsa muy rica y natural", "url"),
            //Food("6", "Ensalada Caesar", "Green Eat", "Ensaladas", 1800.0, 350, 4.0, 45, "Fresca y natural", "url"),
            Food("7", "Sushi Roll", "Sushi Club", "Sushi", 15300.0, 400, 4.9, 142, "Salmón fresco y arroz muy dulce", "url"),
            Food("8", "Pote de 1kg", "Rapanui", "Postres", 14000.0, 350, 4.0, 105, "Incríble pote de 1kg de chocolate y otros sabores", "url"),
        )
        emit(foods)
    }
}
