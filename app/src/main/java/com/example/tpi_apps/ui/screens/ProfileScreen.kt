package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tpi_apps.R
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.logic.ProfileViewModel

import com.example.tpi_apps.logic.ReviewViewModel
import com.example.tpi_apps.ui.components.ReviewItem
import com.example.tpi_apps.ui.components.ReviewListComponent
import com.example.tpi_apps.ui.navigation.Routes

@Composable
fun ProfileScreen(
    user: User,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    onUserUpdated: (User) -> Unit,
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(),
    viewModel: ReviewViewModel
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val userReviews by viewModel.userReviews.collectAsState()
    val isLoadingReviews by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo con gradiente superior azul suave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5C7CFA).copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(40.dp)) }

            // Card del Perfil
            item {
                ProfileHeaderCard(
                    user = user,
                    onEditClick = { showEditDialog = true }
                )
            }

            // Estadísticas (Puntos y Rango)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Puntos",
                        value = "${user.points} pts",
                        subtitle = "${(user.getProgressPercent() * 100).toInt()}% para Rango Oro",
                        icon = Icons.Default.EmojiEvents,
                        modifier = Modifier.weight(1.1f),
                        showProgress = true,
                        progress = user.getProgressPercent()
                    )
                    StatCard(
                        title = "Rango",
                        value = user.level,
                        subtitle = "Activo",
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(0.9f),
                        showStatus = true
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "REPUTACIÓN",
                        value = "${user.reputation}/5.0",
                        subtitle = "Valoración media de tus reseñas",
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f),
                        iconTint = Color(0xFFFFD700),
                        isSmallTitle = true
                    )
                    StatCard(
                        title = "RESEÑAS CREADAS",
                        value = user.reviewCount.toString(),
                        subtitle = "Ver historial >",
                        icon = null,
                        modifier = Modifier.weight(1f),
                        isSmallTitle = true
                    )
                }
            }

            item {
                ContentTabs()
            }

            if (isLoadingReviews) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (userReviews.isEmpty()) {
                // "No hay reseñas"
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_reviews_img),
                            contentDescription = "Sin reseñas",
                            modifier = Modifier.size(125.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Oprime en ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.camaraicon),
                                contentDescription = "Icono cámara",
                                tint = Color(0xFF3A63ED),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = " y sube tu primer reseña!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            } else {
                items(userReviews.size) { index ->
                    ReviewItem(
                        review = userReviews[index],
                        width = null,
                        onClick = { id: String ->
                            navController.navigate(Routes.ReseniaSpecific.createRoute(id))
                        }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showEditDialog) {
        SettingsDialog(
            user = user,
            isDarkTheme = isDarkTheme,
            onToggleDarkTheme = onToggleDarkTheme,
            onClose = { showEditDialog = false },
            onUserUpdated = onUserUpdated,
            profileViewModel = profileViewModel
        )
    }
}

@Composable
fun ProfileHeaderCard(user: User, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Avatar con Emoji basado en la comida
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF3A63ED).copy(alpha = 0.1f))
                        .border(1.dp, Color(0xFF3A63ED).copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getEmojiForFood(user.avatarSeed),
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Comensal Verificado", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A63ED))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.verified_icon),
                            contentDescription = null,
                            tint = Color(0xFF3A63ED),
                            modifier = Modifier.size(10.dp)
                        )
                    }
                    Text(text = user.username, fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    Text(text = user.name, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun getEmojiForFood(seed: String): String {
    return when (seed.lowercase()) {
        "taco" -> "🌮"
        "hamburger" -> "🍔"
        "pizza" -> "🍕"
        "pasta" -> "🍝"
        "helado" -> "🍦"
        "sushi" -> "🍣"
        "ramen" -> "🍜"
        "donuts" -> "🍩"
        "cake" -> "🍰"
        "cookie" -> "🍪"
        else -> "🍔"
    }
}

val foodSeeds = listOf("hamburger", "taco", "pizza", "pasta", "helado", "sushi", "ramen", "donuts", "cake", "cookie")

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    iconTint: Color = Color(0xFF5C7CFA),
    showProgress: Boolean = false,
    progress: Float = 0f,
    showStatus: Boolean = false,
    isSmallTitle: Boolean = false
) {
    Card(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (icon != null && !isSmallTitle) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = if (isSmallTitle) 10.sp else 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (icon != null && !isSmallTitle) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
                icon?.let {
                    Icon(imageVector = it, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
                }
            }

            Column {
                Text(
                    text = value,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (icon != null && !isSmallTitle) Color.White else MaterialTheme.colorScheme.onSurface
                )
                if (showProgress) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape),
                        color = Color(0xFF5C7CFA),
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
                if (showStatus) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4ADE80), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = subtitle, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4ADE80))
                    }
                } else {
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = if (icon != null && !isSmallTitle) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ContentTabs() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TabItem(title = "Reseñas", icon = "✍️", isSelected = true, modifier = Modifier.weight(1f))
        TabItem(title = "Canjes", icon = "🎁", isSelected = false, modifier = Modifier.weight(1f))
    }
}

@Composable
fun TabItem(title: String, icon: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
            .clickable { /* Tab click */ },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SettingsDialog(
    user: User,
    isDarkTheme: Boolean,
    onToggleDarkTheme: (Boolean) -> Unit,
    onClose: () -> Unit,
    onUserUpdated: (User) -> Unit,
    profileViewModel: ProfileViewModel
) {
    var tempUsername by remember { mutableStateOf(user.username) }
    var tempEmail by remember { mutableStateOf(user.email) }
    var currentAvatarSeed by remember { mutableStateOf(user.avatarSeed) }
    val isUpdating by profileViewModel.isUpdating.collectAsState()

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF1E293B)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ajustes de Perfil",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val currentIndex = foodSeeds.indexOf(currentAvatarSeed.lowercase())
                                val nextIndex = (currentIndex + 1) % foodSeeds.size
                                currentAvatarSeed = foodSeeds[nextIndex]
                            }
                            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = getEmojiForFood(currentAvatarSeed), fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Icono de perfil", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Toca para cambiar", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                        }
                    }

                    // Username input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = tempUsername,
                            onValueChange = { tempUsername = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (tempUsername.isEmpty()) {
                                    Text(
                                        text = "Nombre de usuario",
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

                    // Theme toggle
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                profileViewModel.logout()
                                onClose()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Salir", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        Button(
                            onClick = {
                                val userId = user.id
                                if (userId != null) {
                                    profileViewModel.updateProfile(userId, user.name, tempUsername, tempEmail, currentAvatarSeed) { success ->
                                        if (success) {
                                            onUserUpdated(user.copy(username = tempUsername, email = tempEmail, avatarSeed = currentAvatarSeed))
                                            onClose()
                                        }
                                    }
                                }
                            },
                            enabled = !isUpdating,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C7CFA)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            if (isUpdating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Text(text = "Aplicar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    Button(
                        onClick = onClose,
                        enabled = !isUpdating,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5B62)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(text = "Cancelar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
