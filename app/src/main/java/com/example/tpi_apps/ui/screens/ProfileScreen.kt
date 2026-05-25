package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onSettingsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nextLevelPoints = 500
    val progressPercent = (user.points.toFloat() / nextLevelPoints).coerceIn(0f, 1f)
    var currentAvatarIdx by remember { mutableStateOf(0) }
    val avatarOptions = listOf("👤", "🍔", "🍕", "🍰", "🍣", "🌮")
    var subTab by remember { mutableStateOf("options") }
    var showEditDialog by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf(user.username) }
    var tempEmail by remember { mutableStateOf(user.email) }
    val openSettings = {
        showEditDialog = true
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background( Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4E71FA),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF)
                )
            ))
            .padding(16.dp)
    ) {
        // --- BOX 1: INFO DE PERFIL BENTO ---
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(24.dp))
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
                        Text(text = "Comensal Verificado", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                        Text(text = user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        Text(text = user.email, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF64748B))
                    }
                }
                IconButton(onClick = openSettings) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Ajustes", tint = Color(0xFF94A3B8))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- ENLACE BENTO GRID FILA 1: PUNTOS Y RANGO ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            // --- BOX 2: CANASTA DE PUNTOS REESTRUCTURADA ---
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
                            progress = progressPercent,
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

            // --- BOX 3: NIVEL DE CRÍTICO REESTRUCTURADO ---
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

        Spacer(modifier = Modifier.height(10.dp))

        // --- ENLACE BENTO GRID FILA 2: REPUTACIÓN Y RESEÑAS ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            // --- BOX 4: CARD DE REPUTACIÓN ---
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f).height(104.dp).border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Text("Reputación ★", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    Row {
                        Text(text = String.format("%.1f", user.reputation), fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B), modifier = Modifier.alignByBaseline())
                        Text(" /5.0", fontSize = 9.sp, color = Color(0xFF64748B), modifier = Modifier.alignByBaseline())
                    }
                    Text("Valoración media de tus críticas.", fontSize = 8.sp, color = Color(0xFF64748B), lineHeight = 10.sp)
                }
            }

            // --- BOX 5: CARD DE RESEÑAS HISTORIAL ---
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.weight(1f).height(104.dp)
                    .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                    .clickable {
                        subTab = "reviews"
                        onReviewsClick()
                    }
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Reseñas Creadas", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                        Text(text = "↗", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }
                    Text("${user.reviewCount}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
                    Text("Ver historial >", fontSize = 8.5.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SUBTABS PILBAR SELECTOR (BENTO MODE SUB-NATIVE SELECTOR) ---
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0).copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Reseñas Button
                Button(
                    onClick = { subTab = "reviews" },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subTab == "reviews") Color.White else Color.Transparent,
                        contentColor = if (subTab == "reviews") Color(0xFF1E293B) else Color(0xFF64748B)
                    ),
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    elevation = if (subTab == "reviews") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                ) {
                    Text(text = "✍️ Reseñas", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                // Canjes Button
                Button(
                    onClick = { subTab = "points" },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (subTab == "points") Color.White else Color.Transparent,
                    contentColor = if (subTab == "points") Color(0xFF1E293B) else Color(0xFF64748B)
                ),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                elevation = if (subTab == "points") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                ) {
                Text(text = "🎁 Canjes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- DYNAMIC ACCORDION CONTENT IN JETPACK COMPOSE ---
        when (subTab) {
                "points" -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Reward Item 1
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
                                Text("🌮", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cupón - Orden de Tacos", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            }
                            Text("Canjeable en 'El Trompo Dorado'. Válido por 3 tacos al pastor.", fontSize = 9.sp, color = Color(0xFF64748B))
                        }
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.White),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            enabled = user.points >= 200
                        ) {
                            Text(text = "Canjear (200 pts)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Reward Item 2
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
                                Text("🍕", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Descuento en Ristorante", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            }
                            Text("Válido en la compra de pizza familiar. Canjea -400 pts.", fontSize = 9.sp, color = Color(0xFF64748B))
                        }
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.White),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            enabled = user.points >= 400
                        ) {
                            Text("Canjear (400 pts)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
                "reviews" -> {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Tus Reseñas Recientes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))

                // Review Item 1
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)).padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("El Trompo Dorado", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("★", fontSize = 11.sp, color = Color(0xFFF59E0B))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("4.5", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Excelente sazón y los tacos de pastor están riquísimos. Muy recomendados.", fontSize = 9.5.sp, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("23/05/2026 • 20:30", fontSize = 8.sp, color = Color(0xFF94A3B8), fontFamily = FontFamily.Monospace)
                            Text(
                                "👍 14 Likes",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569),
                            modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Review Item 2
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp)).padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Bella Italia Ristorante", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("★", fontSize = 11.sp, color = Color(0xFFF59E0B))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("5.0", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("La pasta carbonara es de otro planeta. La atención es sumamente cordial y rápida.", fontSize = 9.5.sp, color = Color(0xFF475569))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("24/05/2026 • 15:15", fontSize = 8.sp, color = Color(0xFF94A3B8), fontFamily = FontFamily.Monospace)
                            Text(
                                text = "👍 8 Likes",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF475569),
                            modifier = Modifier.background(Color(0xFFF1F5F9), RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
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
                                            text = "Nuevo nombre de usuario",
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
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = {
                                    // Aplicar los cambios mediante lambda o asignación directa
                                    showEditDialog = false
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
}