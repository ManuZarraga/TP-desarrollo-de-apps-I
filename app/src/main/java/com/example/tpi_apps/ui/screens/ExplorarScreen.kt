package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tpi_apps.data.model.Brand
import com.example.tpi_apps.logic.ExplorarViewModel
import com.example.tpi_apps.ui.components.BrandCardSkeleton
import com.example.tpi_apps.ui.components.Hero
import com.example.tpi_apps.ui.components.SectionHeader
import com.example.tpi_apps.ui.navigation.Routes

import com.example.tpi_apps.logic.ReviewViewModel

@Composable
fun ExplorarScreen(
    navController: androidx.navigation.NavController,
    modifier: Modifier = Modifier,
    explorarViewModel: ExplorarViewModel = viewModel(),
    viewModel: ReviewViewModel
) {
    val searchQuery by explorarViewModel.searchQuery.collectAsState()
    val brands by explorarViewModel.filteredBrands.collectAsState()
    val isLoading by explorarViewModel.isLoading.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Hero(
            searchQuery = searchQuery,
            onSearchQueryChange = { explorarViewModel.onSearchQueryChanged(it) },
            placeholderText = "Buscar una marca específica"
        )

        SectionHeader(
            title = "Elegí la marca que te tienta",
            onSeeAllClick = { /* No-op for now */ }
        )

        if (isLoading) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                items(6) { BrandCardSkeleton() }
            }
        } else if (brands.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Cadena o Marca no encontrada",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                items(brands) { brand ->
                    BrandCard(
                        brand = brand,
                        onClick = {
                            navController.navigate(Routes.BrandItems.createRoute(brand.name))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BrandCard(
    brand: Brand,
    onClick: () -> Unit
) {
    val backgroundColor = with(MaterialTheme.colorScheme) {
        remember(brand.backgroundColor) {
            try {
                if (brand.backgroundColor != null) Color(android.graphics.Color.parseColor(brand.backgroundColor))
                else surface
            } catch (e: Exception) {
                surface
            }
        }
    }

    val imageUrl = "https://sathcrjozwcjzsthzomv.supabase.co/storage/v1/object/public/brands/${brand.imageRes}"

    Surface(
        modifier = Modifier
            .aspectRatio(1f)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = if (brand.imageRes?.startsWith("http") == true) brand.imageRes else imageUrl,
                contentDescription = brand.name,
                modifier = Modifier
                    .fillMaxSize(0.6f),
                contentScale = ContentScale.Fit,
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            )
        }
    }
}
