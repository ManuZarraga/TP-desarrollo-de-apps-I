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
    val reviews by viewModel.reviews.collectAsState()
    val likedReviewIds by viewModel.likedReviewIds.collectAsState()
    val foods by viewModel.filteredFoods.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
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
                    items(reviews) { review ->
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
            
            items(foods) { food ->
                FoodItem(
                    food = food,
                    onClick = { brand, item ->
                        navController.navigate(Routes.ReseniaList.createRoute(brand, item))
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
