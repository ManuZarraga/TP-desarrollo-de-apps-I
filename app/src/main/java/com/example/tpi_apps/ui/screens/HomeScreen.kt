package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import com.example.tpi_apps.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.logic.HomeViewModel
import com.example.tpi_apps.ui.components.*

import com.example.tpi_apps.ui.navigation.Routes

@Composable
fun HomeScreen(
    user: User,
    navController: NavController,
    onSettingsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val trendingReviews by viewModel.trendingReviews.collectAsState()
    val likedReviewIds by viewModel.likedReviewIds.collectAsState()
    val foods by viewModel.paginatedFoods.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val categories = viewModel.categories

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            item {
                Hero(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.onSearchQueryChanged(it) }
                )
            }
            item {
                SectionHeader(
                    title = "Realidad vs. Marketing",
                    onSeeAllClick = { navController.navigate("resenia") }
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    if (isLoading) {
                        items(5) { ReviewItemSkeleton() }
                    } else {
                        items(trendingReviews) { review ->
                            ReviewItem(
                                review = review,
                                onLikeClick = { viewModel.toggleLike(it) },
                                isLiked = likedReviewIds.contains(review.id),
                                onClick = { reviewId ->
                                    navController.navigate(Routes.ReseniaSpecific.createRoute(reviewId))
                                }
                            )
                        }
                    }
                }
            }
            
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categories) { category ->
                        val imageRes = when (category) {
                            "Hamburguesas" -> R.drawable.categoria_hamburguesa
                            "Pizzas" -> R.drawable.categoria_pizza
                            "Sushi" -> R.drawable.categoria_sushi
                            "Pastas" -> R.drawable.categoria_pasta
                            "Shawarma" -> R.drawable.categoria_shawarma
                            "Sánguches" -> R.drawable.categoria_sanguche
                            "Postres" -> R.drawable.categoria_helado
                            else -> R.drawable.categoria_hamburguesa // Fallback
                        }
                        
                        CategoryCard(
                            name = category,
                            imageRes = imageRes,
                            isSelected = category == selectedCategory,
                            onClick = { viewModel.onCategorySelected(category) }
                        )
                    }
                }
            }
            
            if (isLoading) {
                items(5) { ReviewListComponentSkeleton() }
            } else {
                items(foods) { food ->
                    FoodItem(
                        food = food,
                        onClick = { brand, item ->
                            navController.navigate(Routes.ReseniaList.createRoute(brand, item))
                        }
                    )
                }
                
                if (totalPages > 1) {
                    item {
                        HomePaginationSection(
                            currentPage = currentPage + 1,
                            totalPages = totalPages,
                            onPageSelected = { viewModel.onPageChanged(it - 1) }
                        )
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HomePaginationSection(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (currentPage > 1) onPageSelected(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous",
                tint = if (currentPage > 1) Color(0xFF3A63ED) else Color.Gray
            )
        }

        for (page in 1..totalPages) {
            val isSelected = page == currentPage
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFF3A63ED) else Color(0xFFE2E8F0))
                    .clickable { onPageSelected(page) }
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    color = if (isSelected) Color.White else Color(0xFF94A3B8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (page < totalPages) Spacer(modifier = Modifier.width(8.dp))
        }

        IconButton(
            onClick = { if (currentPage < totalPages) onPageSelected(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next",
                tint = if (currentPage < totalPages) Color(0xFF3A63ED) else Color.Gray
            )
        }
    }
}
