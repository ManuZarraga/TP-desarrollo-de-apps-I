package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.logic.ExplorarViewModel
import com.example.tpi_apps.ui.components.Hero
import com.example.tpi_apps.ui.components.SectionHeader

@Composable
fun ExplorarScreen(
    modifier: Modifier = Modifier,
    viewModel: ExplorarViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val brands by viewModel.filteredBrands.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Hero(
            searchQuery = searchQuery,
            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
            placeholderText = "Buscar una marca específica"
        )

        SectionHeader(
            title = "Elegí la marca que te tienta",
            onSeeAllClick = { /* TODO */ }
        )

        if (brands.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Cadena o Marca no encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(brands) { brand ->
                    BrandCard(brand = brand)
                }
            }
        }
    }
}

@Composable
fun BrandCard(brand: Brand) {
    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = brand.backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = brand.imageRes),
                contentDescription = brand.name,
                modifier = Modifier
                    .fillMaxSize(0.5f),
                contentScale = ContentScale.Fit
            )
        }
    }
}
