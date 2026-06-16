package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpi_apps.R
import com.example.tpi_apps.logic.ReseniaFilter
import com.example.tpi_apps.logic.ReviewViewModel
import com.example.tpi_apps.ui.components.ReviewItem
import com.example.tpi_apps.ui.components.ReviewItemSkeleton
import com.example.tpi_apps.ui.navigation.Routes

@Composable
fun ReseniaScreen(
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel = viewModel(),
    onReviewClick: (String) -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val reviews by viewModel.pagedReviews.collectAsState()
    val likedReviewIds by viewModel.likedReviewIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A63ED), Color(0xFFFFFFFF))
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.hero_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.30f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    placeholder = {
                        Text(
                            text = "Buscar productos específicos",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF3A63ED),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF3A63ED)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color(0xFF3A63ED),
                        focusedTextColor = Color(0xFF3A63ED),
                        unfocusedTextColor = Color(0xFF3A63ED)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.bell_empty),
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Filters Section
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    FilterChipItem(
                        text = "#Tendencias",
                        isSelected = selectedFilter == ReseniaFilter.TENDENCIAS,
                        onClick = { viewModel.onFilterSelected(ReseniaFilter.TENDENCIAS) }
                    )
                }
                item {
                    FilterChipItem(
                        text = "Nuevas",
                        isSelected = selectedFilter == ReseniaFilter.NUEVAS,
                        onClick = { viewModel.onFilterSelected(ReseniaFilter.NUEVAS) }
                    )
                }
                item {
                    FilterChipItem(
                        text = "Favoritas",
                        isSelected = selectedFilter == ReseniaFilter.FAVORITAS,
                        onClick = { viewModel.onFilterSelected(ReseniaFilter.FAVORITAS) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reviews List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (isLoading) {
                    items(3) { 
                        ReviewItemSkeleton(
                            width = null,
                            modifier = Modifier.fillMaxWidth()
                        ) 
                    }
                } else if (reviews.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_items_photo),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No existen reseñas de este producto",
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    items(reviews) { review ->
                        ReviewItem(
                            review = review,
                            width = null, // To use fillMaxWidth
                            modifier = Modifier.fillMaxWidth(),
                            onLikeClick = { viewModel.toggleLike(it) },
                            isLiked = likedReviewIds.contains(review.id),
                            onClick = { onReviewClick(review.id) }
                        )
                    }

                    item {
                        ReviewPaginationSection(
                            currentPage = currentPage,
                            totalPages = totalPages,
                            onPageSelected = { viewModel.onPageChanged(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .then(
                if (!isSelected) Modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                else Modifier
            ),
        color = if (isSelected) Color(0xFF3A63ED) else Color.White,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun ReviewPaginationSection(
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
