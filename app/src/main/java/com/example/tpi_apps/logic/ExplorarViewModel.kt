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

    private val _brands = MutableStateFlow(
        listOf(
            Brand("McDonald's", R.drawable.mcdonalds, Color(0xFFBD0017)),
            Brand("Burger King", R.drawable.burgerking, Color(0xFFF5ECDD)),
            Brand("Sushi Sur", R.drawable.sushisur, Color(0xFFFFFFFF)),
            Brand("Shami Shawarma", R.drawable.shami_shawarma, Color(0xFFED3237)),
            Brand("La Juvenil", R.drawable.la_juvenil, Color(0xFFCC0000)),
            Brand("Subway", R.drawable.subway, Color(0xFF005543)),
            Brand("Guerrín", R.drawable.guerrin, Color(0xFFFFFFFF)),
            Brand("Rapanui", R.drawable.rapanui, Color(0xFF3D0739))
        )
    )

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
