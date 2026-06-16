package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.FoodRepository
import com.example.tpi_apps.data.repository.ReviewRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrandItemsViewModel(
    private val brandName: String,
    private val foodRepository: FoodRepository = FoodRepository.getInstance(),
    private val reviewRepository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val brandReviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _foods = MutableStateFlow<List<Food>>(emptyList())

    val filteredFoods: StateFlow<List<Food>> = combine(_foods, _searchQuery) { foods, query ->
        foods.filter { food ->
            val matchesBrand = food.restaurant.contains(brandName, ignoreCase = true)
            val matchesQuery = food.name.contains(query, ignoreCase = true) ||
                               (food.description?.contains(query, ignoreCase = true) == true)
            matchesBrand && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pagedFoods: StateFlow<List<Food>> = combine(filteredFoods, _currentPage) { foods, page ->
        val startIndex = (page - 1) * 5
        val endIndex = minOf(startIndex + 5, foods.size)
        if (startIndex < foods.size) foods.subList(startIndex, endIndex) else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPages: StateFlow<Int> = filteredFoods.map { 
        val pages = (it.size + 4) / 5
        if (pages == 0) 1 else pages
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    init {
        loadData()
    }

    private fun loadData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                foodRepository.getFoods().collect {
                    _foods.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
        loadReviews()
    }

    private fun loadReviews() {
        viewModelScope.launch {
            reviewRepository.getReviews().collect { allReviews ->
                _reviews.value = allReviews.filter { 
                    it.restaurantName.contains(brandName, ignoreCase = true)
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _currentPage.value = 1
    }

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }
}
