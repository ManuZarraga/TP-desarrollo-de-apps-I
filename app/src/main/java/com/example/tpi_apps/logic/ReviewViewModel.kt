package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.ReviewRepository
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

    init {
        loadReviews()
        loadUserLikes()
    }

    private fun loadReviews() {
        viewModelScope.launch {
            repository.getReviews().collect {
                _allReviews.value = it
            }
        }
    }

    private fun loadUserLikes() {
        viewModelScope.launch {
            repository.loadUserLikes("b46a5b6d-9276-47a2-9721-6925000552b7")
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: ReseniaFilter) {
        _selectedFilter.value = filter
    }

    fun toggleLike(reviewId: String) {
        viewModelScope.launch {
            // Usuario de prueba fijo para coincidir con CrearReseniaViewModel
            val currentUserId = "b46a5b6d-9276-47a2-9721-6925000552b7"
            repository.toggleLike(reviewId, currentUserId)
            // Recargar reseñas para actualizar el contador de likes de la base de datos
            loadReviews()
        }
    }

    fun getUserReviews(username: String): StateFlow<List<Review>> = _allReviews
        .map { reviews -> reviews.filter { it.username.contains(username, ignoreCase = true) || it.profiles?.username?.contains(username, ignoreCase = true) == true } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getItemReviews(brandName: String, itemName: String): StateFlow<List<Review>> = _allReviews
        .map { reviews -> reviews.filter { it.restaurantName == brandName && it.itemName == itemName } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getReviewById(reviewId: String): StateFlow<Review?> = _allReviews
        .map { reviews -> reviews.find { it.id == reviewId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateLikes(reviewId: String) {
        toggleLike(reviewId)
    }

    val userReviews: StateFlow<List<Review>> = _allReviews
        .map { reviews -> reviews.filter { it.profiles?.username == "federicodip" || it.username == "Federico Dip" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}