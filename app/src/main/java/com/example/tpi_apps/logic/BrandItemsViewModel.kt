package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.repository.FoodRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrandItemsViewModel(
    private val brandName: String,
    private val foodRepository: FoodRepository = FoodRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _foods = MutableStateFlow<List<Food>>(emptyList())

    val filteredFoods: StateFlow<List<Food>> = combine(_foods, _searchQuery) { foods, query ->
        foods.filter { food ->
            val matchesBrand = food.restaurant.contains(brandName, ignoreCase = true)
            val matchesQuery = food.name.contains(query, ignoreCase = true) || 
                               food.description.contains(query, ignoreCase = true)
            matchesBrand && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadFoods()
    }

    private fun loadFoods() {
        viewModelScope.launch {
            foodRepository.getFoods().collect {
                _foods.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
