package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.repository.BrandRepository
import com.example.tpi_apps.data.repository.FoodRepository
import com.example.tpi_apps.data.repository.ReviewRepository
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class CrearReseniaViewModel : ViewModel() {
    private val reviewRepository = ReviewRepository.getInstance()
    private val brandRepository = BrandRepository.getInstance()
    private val foodRepository = FoodRepository.getInstance()

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands.asStateFlow()

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    private val _uploading = MutableStateFlow(false)
    val uploading: StateFlow<Boolean> = _uploading.asStateFlow()

    init {
        loadBrands()
        loadFoods()
    }

    private fun loadBrands() {
        viewModelScope.launch {
            brandRepository.getBrands().collect {
                _brands.value = it
            }
        }
    }

    private fun loadFoods() {
        viewModelScope.launch {
            foodRepository.getFoods().collect {
                _foods.value = it
            }
        }
    }

    suspend fun uploadImage(byteArray: ByteArray): String? {
        val fileName = "${UUID.randomUUID()}.jpg"
        return try {
            _uploading.value = true
            val bucket = SupabaseModule.client.storage["reviews"]
            bucket.upload(fileName, byteArray)
            "https://sathcrjozwcjzsthzomv.supabase.co/storage/v1/object/public/reviews/$fileName"
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            _uploading.value = false
        }
    }

    fun submitReview(
        user: User,
        brandId: String,
        foodId: String,
        rating: Int,
        comment: String,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            val now = LocalDate.now()
            val time = LocalTime.now()
            
            val newReview = Review(
                id = UUID.randomUUID().toString(),
                userId = "b46a5b6d-9276-47a2-9721-6925000552b7", // Usuario de prueba fijo por ahora
                brandId = brandId,
                foodId = foodId,
                rating = rating,
                comment = comment,
                imageUrl = imageUrl,
                date = now.format(DateTimeFormatter.ISO_DATE),
                time = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                likes = 0
            )

            reviewRepository.addReview(newReview)
        }
    }
}
