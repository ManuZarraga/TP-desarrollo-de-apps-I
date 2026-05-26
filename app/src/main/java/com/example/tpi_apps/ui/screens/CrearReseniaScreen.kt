package com.example.tpi_apps.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpi_apps.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReseniaScreen(
    user: User,
    onSettingsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nextLevelPoints = 500
    val progressPercent = (user.points.toFloat() / nextLevelPoints).coerceIn(0f, 1f)
    var currentAvatarIdx by remember { mutableStateOf(0) }
    val avatarOptions = listOf("👤", "🍔", "🍕", "🍰", "🍣", "🌮")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
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
                        Text("Comensal Verificado", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                        Text(text = user.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        Text(text = user.email, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF64748B))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

    }
}