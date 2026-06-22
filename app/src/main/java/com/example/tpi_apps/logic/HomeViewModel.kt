package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Food
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.FoodRepository
import com.example.tpi_apps.data.repository.ReviewRepository
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val foodRepository: FoodRepository = FoodRepository.getInstance(),
    private val reviewRepository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val trendingReviews: StateFlow<List<Review>> = _reviews.map { reviews: List<Review> ->
        reviews.sortedByDescending { it.likes }.take(5)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val likedReviewIds: StateFlow<Set<String>> = reviewRepository.likedReviewIds

    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    
    private val _selectedCategory = MutableStateFlow("Hamburguesas")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val itemsPerPage = 4

    val filteredFoods: StateFlow<List<Food>> = combine(_foods, _selectedCategory, _searchQuery) { foods, category, query ->
        foods.filter { food ->
            val matchesCategory = food.category == category
            val matchesQuery = food.name.contains(query, ignoreCase = true) || 
                               (food.description?.contains(query, ignoreCase = true) == true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val paginatedFoods: StateFlow<List<Food>> = combine(filteredFoods, _currentPage) { foods, page ->
        val fromIndex = page * itemsPerPage
        val toIndex = (fromIndex + itemsPerPage).coerceAtMost(foods.size)
        if (fromIndex < foods.size) {
            foods.subList(fromIndex, toIndex)
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPages: StateFlow<Int> = filteredFoods.combine(MutableStateFlow(itemsPerPage)) { foods, limit ->
        if (foods.isEmpty()) 0 else kotlin.math.ceil(foods.size.toDouble() / limit).toInt()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val categories: StateFlow<List<String>> = _foods.map { foods ->
        foods.map { it.category }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadData()
        observeCategories()
    }

    private fun observeCategories() {
        viewModelScope.launch {
            categories.collect { cats ->
                if (cats.isNotEmpty() && _selectedCategory.value !in cats) {
                    _selectedCategory.value = cats.first()
                }
            }
        }
    }

    private fun loadData() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val foodsJob = launch {
                    foodRepository.getFoods().collect { _foods.value = it }
                }
                val reviewsJob = launch {
                    reviewRepository.getReviews().collect { _reviews.value = it }
                }
                foodsJob.join()
                reviewsJob.join()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleLike(reviewId: String) {
        viewModelScope.launch {
            val currentUserId = SupabaseModule.client.auth.currentUserOrNull()?.id ?: return@launch
            
            // Actualización optimista de la lista local
            val isCurrentlyLiked = likedReviewIds.value.contains(reviewId)
            val previousReviews = _reviews.value

            _reviews.value = _reviews.value.map { review ->
                if (review.id == reviewId) {
                    val newLikes = if (isCurrentlyLiked) (review.likes - 1).coerceAtLeast(0) else review.likes + 1
                    review.copy(likes = newLikes)
                } else review
            }

            val success = reviewRepository.toggleLike(reviewId, currentUserId)
            
            if (!success) {
                // Revertir si falla
                _reviews.value = previousReviews
            }
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        _currentPage.value = 0
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _currentPage.value = 0
    }

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }
}
