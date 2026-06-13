package com.example.tpi_apps.ui.screens

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tpi_apps.R
import com.example.tpi_apps.data.model.Review
import com.example.tpi_apps.logic.ReviewViewModel

@Composable
fun ReseniaSpecificScreen(
    reviewId: String,
    navController: NavController,
    viewModel: ReviewViewModel = viewModel()
) {
    val review by remember(reviewId) { viewModel.getReviewById(reviewId) }.collectAsStateWithLifecycle()
    val likedReviewIds by viewModel.likedReviewIds.collectAsStateWithLifecycle()
    val isLiked = review?.let { likedReviewIds.contains(it.id) } ?: false
    val context = LocalContext.current

    val playLikeSound = {
        try {
            val mp = MediaPlayer.create(context, R.raw.like_sound)
            mp.setOnCompletionListener { it.release() }
            mp.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Box(
        modifier = Modifier
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

        review?.let { currentReview ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .background(Color.White, CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = Color.Black
                        )
                    }
                }

                // Main Content Card
                ProductCard(currentReview)

                // User Review Card
                UserReviewCard(currentReview, isLiked)

                // Description Card (con el botón de Like integrado ahora)
                DescriptionCard(
                    description = currentReview.itemDescription,
                    isLiked = isLiked,
                    onLikeClick = {
                        viewModel.updateLikes(currentReview.id)
                        if (!isLiked) playLikeSound()
                    }
                )

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun ProductCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val reviewImageUrl = review.imageUrl?.let {
                    if (it.startsWith("http")) it
                    else "https://sathcrjozwcjzsthzomv.supabase.co/storage/v1/object/public/reviews/$it"
                }

                val foodImageUrl = review.foods?.imageUrl?.let {
                    if (it.startsWith("http")) it
                    else "https://sathcrjozwcjzsthzomv.supabase.co/storage/v1/object/public/foods/$it"
                }

                val finalImageUrl = reviewImageUrl ?: foodImageUrl

                Box(modifier = Modifier.weight(1.5f)) {
                    AsyncImage(
                        model = finalImageUrl,
                        contentDescription = review.itemName,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("1/3", color = Color.White, fontSize = 10.sp)
                    }
                }
                
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AsyncImage(
                        model = finalImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    AsyncImage(
                        model = finalImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = review.itemName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.restaurantName,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.verified_icon),
                    contentDescription = null,
                    tint = Color(0xFF3A63ED),
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = "$${review.itemPrice ?: "0.0"}",
                color = Color(0xFF3A63ED),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun UserReviewCard(review: Review, isLiked: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Brush.linearGradient(colors = listOf(Color(0xFF2563EB), Color(0xFF4F46E5)))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🍔", fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Comensal Verificado",
                        color = Color(0xFF3A63ED),
                        fontSize = 12.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_selected),
                            contentDescription = null,
                            tint = Color(0xFFFF7E1C),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Súper Gourmet",
                            color = Color(0xFFFF7E1C),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Rating Stars
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { index ->
                    Icon(
                        painter = painterResource(
                            id = if (index < review.rating) R.drawable.star_selected else R.drawable.star_unselected
                        ),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = review.rating.toDouble().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF3A63ED)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment ?: "",
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFE0E7FF).copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.like_icon),
                        contentDescription = null,
                        tint = Color(0xFF3A63ED),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${review.likes}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3A63ED)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isLiked) "Te pareció útil esta reseña" else "A ${review.likes} usuarios les pareció útil esta reseña",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionCard(description: String, isLiked: Boolean, onLikeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Descripción del producto",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF64748B)
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onLikeClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLiked) Color(0xFFE0E7FF) else Color(0xFF3A63ED)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.like_icon),
                        contentDescription = null,
                        tint = if (isLiked) Color(0xFF3A63ED) else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isLiked) "Ya no me parece útil" else "Me pareció útil",
                        color = if (isLiked) Color(0xFF3A63ED) else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
