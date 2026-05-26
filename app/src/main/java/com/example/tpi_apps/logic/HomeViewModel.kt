package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.FoodRepository
import com.example.tpi_apps.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val foodRepository: FoodRepository = FoodRepository(),
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    
    private val _selectedCategory = MutableStateFlow("Hamburguesas")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredFoods: StateFlow<List<Food>> = combine(_foods, _selectedCategory, _searchQuery) { foods, category, query ->
        foods.filter { food ->
            val matchesCategory = food.category == category
            val matchesQuery = food.name.contains(query, ignoreCase = true) || 
                               food.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = listOf("Hamburguesas", "Pizza", "Sushi", "Pastas", "Shawarma", "Postres")

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

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
