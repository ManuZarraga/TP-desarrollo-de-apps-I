package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.FoodRepository
import com.example.tpi_apps.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val foodRepository: FoodRepository = FoodRepository(),
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> = _foods.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todo")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val categories = listOf("Todo", "Hamburguesas", "Pizza", "Sushi", "Ensaladas", "Postres")

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            foodRepository.getFoods().collect {
                _foods.value = it
            }
        }
        viewModelScope.launch {
            reviewRepository.getReviews().collect {
                _reviews.value = it
            }
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }
}
