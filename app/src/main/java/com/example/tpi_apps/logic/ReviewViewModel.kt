package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.data.repository.ReviewRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ReviewViewModel(
    private val repository: ReviewRepository = ReviewRepository.getInstance()
) : ViewModel() {

    fun getUserReviews(username: String): StateFlow<List<Review>> = repository
        .getReviews()
        .map { reviews -> reviews.filter { it.username == username } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getItemReviews(brandName: String, itemName: String): StateFlow<List<Review>> = repository
        .getReviews()
        .map { reviews -> reviews.filter { it.restaurantName == brandName && it.itemName == itemName } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getReviewById(reviewId: String): StateFlow<Review?> = repository
        .getReviews()
        .map { reviews -> reviews.find { it.id == reviewId } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateLikes(reviewId: String) {
        repository.updateLikes(reviewId)
    }

    val userReviews: StateFlow<List<Review>> = repository
        .getReviews()
        .map { reviews -> reviews.filter { it.username == "Federico Dip" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}