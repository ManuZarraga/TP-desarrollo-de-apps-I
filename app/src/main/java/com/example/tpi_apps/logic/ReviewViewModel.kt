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

    val userReviews: StateFlow<List<Review>> = repository
        .getReviews()
        .map { reviews -> reviews.filter { it.username == "Federico Dip" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}