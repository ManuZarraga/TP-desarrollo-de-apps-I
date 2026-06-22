package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.network.SupabaseModule
import com.example.tpi_apps.data.repository.ReviewRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*


enum class ReseniaFilter {
    TENDENCIAS, NUEVAS, FAVORITAS
}
class ReviewViewModel(
    private val repository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel()
 {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(ReseniaFilter.TENDENCIAS)
    val selectedFilter: StateFlow<ReseniaFilter> = _selectedFilter.asStateFlow()

    private val _allReviews = MutableStateFlow<List<Review>>(emptyList())

    val likedReviewIds: StateFlow<Set<String>> = repository.likedReviewIds

    val filteredReviews: StateFlow<List<Review>> = combine(
        _allReviews, _searchQuery, _selectedFilter, likedReviewIds
    ) { reviews, query, filter, likedIds ->
        var result = reviews.filter {
            it.restaurantName.contains(query, ignoreCase = true) ||
                    it.itemName.contains(query, ignoreCase = true) ||
                    (it.comment?.contains(query, ignoreCase = true) == true)
        }

        result = when (filter) {
            ReseniaFilter.TENDENCIAS -> result.sortedByDescending { it.likes }
            ReseniaFilter.NUEVAS -> result.sortedByDescending { it.displayDate }
            ReseniaFilter.FAVORITAS -> result.filter { likedIds.contains(it.id) }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pagedReviews: StateFlow<List<Review>> = combine(filteredReviews, _currentPage) { reviews, page ->
        val startIndex = (page - 1) * 5
        val endIndex = minOf(startIndex + 5, reviews.size)
        if (startIndex < reviews.size) reviews.subList(startIndex, endIndex) else emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPages: StateFlow<Int> = filteredReviews.map { 
        val pages = (it.size + 4) / 5
        if (pages == 0) 1 else pages
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    init {
        loadReviews()
        loadUserLikes()
    }

    private fun loadReviews() {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getReviews().collect {
                _allReviews.value = it
                _isLoading.value = false
            }
        }
    }

    private fun loadUserLikes() {
        viewModelScope.launch {
            val currentUserId = SupabaseModule.client.auth.currentUserOrNull()?.id
            if (currentUserId != null) {
                repository.loadUserLikes(currentUserId)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: ReseniaFilter) {
        _selectedFilter.value = filter
        _currentPage.value = 1
    }

    fun onPageChanged(page: Int) {
        _currentPage.value = page
    }

    fun toggleLike(reviewId: String) {
        viewModelScope.launch {
            val currentUserId = SupabaseModule.client.auth.currentUserOrNull()?.id ?: return@launch
            val isCurrentlyLiked = likedReviewIds.value.contains(reviewId)
            val previousReviews = _allReviews.value

            _allReviews.value = _allReviews.value.map { review ->
                if (review.id == reviewId) {
                    val newLikes = if (isCurrentlyLiked) (review.likes - 1).coerceAtLeast(0) else review.likes + 1
                    review.copy(likes = newLikes)
                } else review
            }

            val success = repository.toggleLike(reviewId, currentUserId)

            if (!success) {
                _allReviews.value = previousReviews
            }
        }
    }

    fun getUserReviews(username: String): StateFlow<List<Review>> = _allReviews
        .map { reviews -> reviews.filter { it.username.contains(username, ignoreCase = true) || it.profiles?.username?.contains(username, ignoreCase = true) == true } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getReviewById(reviewId: String): StateFlow<Review?> = _allReviews
        .map { reviews -> 
            val review = reviews.find { it.id == reviewId }
            if (review == null && _allReviews.value.isNotEmpty()) {

            }
            review
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _allReviews.value.find { it.id == reviewId }
        )

    fun getItemReviews(brandName: String, itemName: String): StateFlow<List<Review>> = combine(_allReviews, _currentPage) { reviews, page ->
        val filtered = reviews.filter { it.restaurantName == brandName && it.itemName == itemName }
        val startIndex = (page - 1) * 5
        val endIndex = minOf(startIndex + 5, filtered.size)
        if (startIndex < filtered.size) filtered.subList(startIndex, endIndex) else emptyList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getTotalPagesForItem(brandName: String, itemName: String): StateFlow<Int> = _allReviews
        .map { reviews -> 
            val count = reviews.count { it.restaurantName == brandName && it.itemName == itemName }
            val pages = (count + 4) / 5
            if (pages == 0) 1 else pages
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    fun updateLikes(reviewId: String) {
        toggleLike(reviewId)
    }

    val userReviews: StateFlow<List<Review>> = _allReviews
        .map { reviews -> 
            val currentUserId = SupabaseModule.client.auth.currentUserOrNull()?.id
            reviews.filter { it.userId == currentUserId } 
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}