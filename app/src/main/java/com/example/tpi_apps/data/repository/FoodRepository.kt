package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepository {
    fun getFoods(): Flow<List<Food>> = flow {
        try {
            val foods = SupabaseModule.client.postgrest["foods"]
                .select()
                .decodeList<Food>()
            emit(foods)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }
}
