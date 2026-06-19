package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpi_apps.data.model.User
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpi_apps.logic.ReviewViewModel
import com.example.tpi_apps.ui.components.ReviewItem
import com.example.tpi_apps.ui.navigation.Routes
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.example.tpi_apps.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardItem(coupon: Coupon, userPoints: Int, onCanjearClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)).padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(coupon.icon, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(coupon.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                }
                Text(coupon.description, fontSize = 9.sp, color = Color(0xFF64748B))
            }
            Button(
                onClick = onCanjearClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.White),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                enabled = userPoints >= coupon.cost
            ) {
                Text(text = "Canjear (${coupon.cost} pts)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PaginationControls(
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

data class Coupon(val icon: String, val title: String, val description: String, val cost: Int, val restaurant: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val nextLevelPoints = 500
    val progressPercent = (user.points.toFloat() / nextLevelPoints).coerceIn(0f, 1f)
    var currentAvatarIdx by remember { mutableIntStateOf(0) }
    val avatarOptions = listOf("👤", "🍔", "🍕", "🍰", "🍣", "🌮")
    var subTab by remember { mutableStateOf("reviews") }
    var reviewsPage by remember { mutableIntStateOf(1) }
    var couponsPage by remember { mutableIntStateOf(1) }
    val itemsPerPage = 4

    var showEditDialog by remember { mutableStateOf(false) }
    var tempName by remember { mutableStateOf(user.name) }
    var tempEmail by remember { mutableStateOf(user.email) }
    val openSettings = {
        showEditDialog = true
    }

    val viewModel: ReviewViewModel = viewModel()
    val userReviews by viewModel.getUserReviews(user.name).collectAsStateWithLifecycle()
    val likedReviewIds by viewModel.likedReviewIds.collectAsStateWithLifecycle()

    val totalPagesReviews = (userReviews.size + itemsPerPage - 1).coerceAtLeast(itemsPerPage) / itemsPerPage
    val paginatedReviews = userReviews.drop((reviewsPage - 1) * itemsPerPage).take(itemsPerPage)

    val allCoupons = remember {
        listOf(
            Coupon("🌮", "Cupón - Orden de Tacos", "Canjeable en 'El Trompo Dorado'. Válido por 3 tacos al pastor.", 200, "El Trompo Dorado"),
            Coupon("🍕", "Descuento en Ristorante", "Válido en la compra de pizza familiar. Canjea -400 pts.", 400, "Ristorante"),
            Coupon("🍔", "Burger Gratis", "Canjeable en 'Big Burger'. Válido por una Burger Simple.", 300, "Big Burger"),
            Coupon("🍣", "Rolls de Regalo", "Válido en 'Sushi Zen'. 6 rolls a elección.", 500, "Sushi Zen"),
            Coupon("🍦", "Postre Helado", "Cualquier sabor en 'Heladería Artic'.", 150, "Heladería Artic"),
            Coupon("🥤", "Bebida Grande", "En la compra de cualquier combo en 'FastFood'.", 100, "FastFood")
        )
    }
    val totalPagesCoupons = (allCoupons.size + itemsPerPage - 1).coerceAtLeast(itemsPerPage) / itemsPerPage
    val paginatedCoupons = allCoupons.drop((couponsPage - 1) * itemsPerPage).take(itemsPerPage)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background( Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.background
                )
            )),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(56.dp)) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp))
                                    .background(Brush.linearGradient(colors = listOf(Color(0xFF2563EB), Color(0xFF4F46E5))))
                            ) {
                                Text(text = avatarOptions[currentAvatarIdx], fontSize = 28.sp)
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(22.dp).align(Alignment.BottomEnd).offset(4.dp, 4.dp).clip(CircleShape)
                                    .background(Color(0xFF1E293B)).clickable { currentAvatarIdx = (currentAvatarIdx + 1) % avatarOptions.size }
                            ) {
                                Text("🔄", fontSize = 10.sp, color = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Comensal Verificado", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(text = user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(text = user.email, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    IconButton(onClick = openSettings) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Ajustes", tint = Color(0xFF94A3B8))
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f).height(116.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF1E1B4B)
                                )
                            ), RoundedCornerShape(20.dp)
                        )
                        .border(1.dp, Color(0xFF4F46E5).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🏆", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Puntos", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF818CF8))
                        }
                        Row {
                            Text("${user.points}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White, modifier = Modifier.alignByBaseline())
                            Text(" pts", fontSize = 10.sp, color = Color(0xFF818CF8), modifier = Modifier.alignByBaseline())
                        }
                        Column {
                            LinearProgressIndicator(
                                progress = { progressPercent },
                                color = Color(0xFF60A5FA),
                                trackColor = Color(0xFF1E293B),
                                modifier = Modifier.fillMaxWidth().height(4.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${(progressPercent * 100).toInt()}% para Rango Oro", fontSize = 8.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }

                val isGold = user.level.contains("Oro") || user.level.contains("Súper")
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f).height(116.dp)
                        .background(
                            Brush.linearGradient(
                                colors = if (isGold) listOf(
                                    Color(0xFF0F172A),
                                    Color(0xFF451A03)
                                ) else listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                            ), RoundedCornerShape(20.dp)
                        )
                        .border(
                            1.dp,
                            if (isGold) Color(0xFFF59E0B).copy(alpha = 0.3f) else Color(0xFF334155),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (isGold) Color(0xFFF59E0B) else Color(0xFF94A3B8),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Rango",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                        Text(
                            text = user.level,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isGold) Color(0xFFF59E0B) else Color(0xFFE2E8F0)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(
                                Color.Black.copy(0.3f),
                                RoundedCornerShape(6.dp)
                            ).padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(6.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Activo", fontSize = 8.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.weight(1f).height(104.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("REPUTACIÓN", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 0.8.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("★", fontSize = 10.sp, color = Color(0xFFF59E0B))
                        }
                        Row {
                            Text(
                                text = String.format(java.util.Locale.getDefault(), "%.1f", user.reputation),
                                fontSize = 28.sp, fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.alignByBaseline()
                            )
                            Text(
                                "/5.0",
                                fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                        Text("Valoración media de tus reseñas", fontSize = 8.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.weight(1f).height(104.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                        .clickable {
                            subTab = "reviews"
                        }
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "RESEÑAS CREADAS",
                            fontSize = 9.sp, fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 0.8.sp
                        )
                        Text(
                            "${user.reviewCount}",
                            fontSize = 28.sp, fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("Ver historial >", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        item {
            // --- SUBTABS PILBAR SELECTOR ---
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { subTab = "reviews" },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (subTab == "reviews") MaterialTheme.colorScheme.surface else Color.Transparent,
                            contentColor = if (subTab == "reviews") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        elevation = if (subTab == "reviews") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text(text = "✍️ Reseñas", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { subTab = "points" },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (subTab == "points") MaterialTheme.colorScheme.surface else Color.Transparent,
                            contentColor = if (subTab == "points") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        elevation = if (subTab == "points") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text(text = "🎁 Canjes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (subTab == "points") {
            items(paginatedCoupons) { coupon ->
                RewardItem(coupon = coupon, userPoints = user.points, onCanjearClick = { /* Handle redeem */ })
            }
            item {
                PaginationControls(
                    currentPage = couponsPage,
                    totalPages = totalPagesCoupons,
                    onPageSelected = { couponsPage = it }
                )
            }
        } else {
            item {
                Text(
                    "Tus Reseñas Recientes",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            items(paginatedReviews) { review ->
                ReviewItem(
                    review = review,
                    width = null,
                    onLikeClick = { viewModel.toggleLike(it) },
                    isLiked = likedReviewIds.contains(review.id),
                    onClick = { reviewId ->
                        navController.navigate(Routes.ReseniaSpecific.createRoute(reviewId))
                    }
                )
            }
            item {
                PaginationControls(
                    currentPage = reviewsPage,
                    totalPages = totalPagesReviews,
                    onPageSelected = { reviewsPage = it }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }

    if (showEditDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showEditDialog = false }) {
            Box(
                modifier = Modifier
                    .width(290.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF4E71FA), Color(0xFF94ADFE), Color(0xFFF5F7FF))
                        )
                    )
                    .padding(24.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Username input card style
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (tempName.isEmpty()) {
                                    Text(
                                        text = "Nuevo nombre",
                                        color = Color(0xFF94A3B8),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // Email input card style
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = tempEmail,
                            onValueChange = { tempEmail = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (tempEmail.isEmpty()) {
                                    Text(
                                        text = "Nuevo email",
                                        color = Color(0xFF94A3B8),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // Action buttons in a Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isDarkTheme) "Modo Oscuro" else "Modo Claro",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = { onToggleDarkTheme(!isDarkTheme) },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                painter = painterResource(id = if (isDarkTheme) R.drawable.moon else R.drawable.sun),
                                contentDescription = "Toggle Dark Mode",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Botones de acción originales
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                showEditDialog = false
                                user.name = tempName
                                user.email = tempEmail
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C7CFA)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Aplicar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = { showEditDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5B62)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Text(
                                text = "Cancelar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
