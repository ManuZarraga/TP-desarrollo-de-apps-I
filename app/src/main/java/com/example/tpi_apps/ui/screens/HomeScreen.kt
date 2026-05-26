package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.logic.HomeViewModel
import com.example.tpi_apps.ui.components.*

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
    val foods by viewModel.foods.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories = viewModel.categories

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            item {
                Hero()
            }
            
            item {
                SectionHeader(
                    title = "Realidad vs. Marketing",
                    onSeeAllClick = onReviewsClick
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(end = 16.dp)
                ) {
                    items(reviews) { review ->
                        ReviewItem(review = review)
                    }
                }
            }
            
            item {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category == selectedCategory,
                            onClick = { viewModel.onCategorySelected(category) }
                        )
                    }
                }
            }
            
            items(foods) { food ->
                FoodItem(food = food)
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
