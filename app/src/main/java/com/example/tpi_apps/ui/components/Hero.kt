package com.example.tpi_apps.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpi_apps.R

data class HeroSlide(
    val title: String,
    val subtitle: String,
    val imageRes: Int
)

@Composable
fun Hero(modifier: Modifier = Modifier) {
    val slides = listOf(
        HeroSlide(
            "Descubrí la verdadera cara de tus pedidos",
            "Revisá las últimas reseñas de tu plato favorito",
            R.drawable.heroimg1
        ),
        HeroSlide(
            "Contanos más acerca de tu pedido",
            "Dejanos tu reseña a continuación ¡No olvides agregar una foto!",
            R.drawable.heroimg2
        ),
        HeroSlide(
            "¿Todavía estás con dudas?",
            "Enterate lo que opinan los demás sobre sus pedidos",
            R.drawable.heroimg3
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(Color(0xFF3A63ED))
    ) {
        // Background Pattern
        Image(
            painter = painterResource(id = R.drawable.hero_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.5f
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar: Search and Notification
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Buscar productos específicos",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.bell_empty),
                    contentDescription = "Notifications",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Slidable Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val slide = slides[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.65f)
                            .padding(bottom = 40.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = slide.title,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = slide.subtitle,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }

                    Image(
                        painter = painterResource(id = slide.imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(170.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Animated Dots Indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(slides.size) { index ->
                val isSelected = pagerState.currentPage == index
                val width = animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    label = "dotWidth"
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width.value)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}
