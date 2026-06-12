package com.example.tpi_apps.data.repository

import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.data.network.SupabaseModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BrandRepository {
    fun getBrands(): Flow<List<Brand>> = flow {
        try {
            val brands = SupabaseModule.apiService.getBrands()
            emit(brands)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: BrandRepository? = null

        fun getInstance(): BrandRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BrandRepository().also { INSTANCE = it }
            }
        }
    }
}
