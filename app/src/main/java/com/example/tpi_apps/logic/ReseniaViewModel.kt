package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.ReviewRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ReseniaFilter {
    TENDENCIAS, NUEVAS, FAVORITAS
}

class ReseniaViewModel(
    private val reviewRepository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(ReseniaFilter.TENDENCIAS)
    val selectedFilter: StateFlow<ReseniaFilter> = _selectedFilter.asStateFlow()

    private val _allReviews = MutableStateFlow<List<Review>>(emptyList())
    
    private val _likedReviewIds = MutableStateFlow<Set<String>>(emptySet())
    val likedReviewIds: StateFlow<Set<String>> = _likedReviewIds.asStateFlow()

    val filteredReviews: StateFlow<List<Review>> = combine(
        _allReviews, _searchQuery, _selectedFilter, _likedReviewIds
    ) { reviews, query, filter, likedIds ->
        var result = reviews.filter {
            it.restaurantName.contains(query, ignoreCase = true) ||
            it.itemName.contains(query, ignoreCase = true) ||
            it.comment.contains(query, ignoreCase = true)
        }

        result = when (filter) {
            ReseniaFilter.TENDENCIAS -> result.sortedByDescending { it.likes }
            ReseniaFilter.NUEVAS -> result.sortedByDescending { it.date }
            ReseniaFilter.FAVORITAS -> result.filter { likedIds.contains(it.id) }
        }
        
        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadReviews()
    }

    private fun loadReviews() {
        viewModelScope.launch {
            reviewRepository.getReviews().collect {
                _allReviews.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: ReseniaFilter) {
        _selectedFilter.value = filter
    }

    fun toggleLike(reviewId: String) {
        val current = _likedReviewIds.value
        if (current.contains(reviewId)) {
            _likedReviewIds.value = current - reviewId
        } else {
            _likedReviewIds.value = current + reviewId
        }
    }
}
