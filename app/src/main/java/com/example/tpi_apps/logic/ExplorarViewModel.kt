package com.example.tpi_apps.logic

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.tpi_apps.R
import com.example.tpi_apps.data.model.Brand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class ExplorarViewModel : ViewModel() {

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands.asStateFlow()

    fun setBrands(brands: List<Brand>) {
        _brands.value = brands
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredBrands = combine(_brands, _searchQuery) { brands, query ->
        if (query.isEmpty()) {
            brands
        } else {
            brands.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}
