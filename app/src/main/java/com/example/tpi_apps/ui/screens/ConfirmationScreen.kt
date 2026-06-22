package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tpi_apps.R
import kotlinx.coroutines.delay

@Composable
fun ConfirmationScreen(
    navController: NavController
) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("inicio") {
            popUpTo("inicio") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (androidx.compose.foundation.isSystemInDarkTheme()) {
                        listOf(Color(0xFF1A1C1E), Color(0xFF121212))
                    } else {
                        listOf(Color(0xFF3A63ED), Color(0xFFFFFFFF))
                    }
                )
            )
    ) {
        if (!androidx.compose.foundation.isSystemInDarkTheme()) {
            Image(
                painter = painterResource(id = R.drawable.hero_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            
            Text(
                text = "¡Reseña cargada\nexitosamente!",
                color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White else Color(0xFF1E3A8A),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Image(
                painter = painterResource(id = R.drawable.review_uploaded),
                contentDescription = "Review Uploaded",
                modifier = Modifier.size(280.dp)
            )

            Text(
                text = "Recordá que podes editar tu post y agregar nuevas fotos cuando quieras",
                color = if (androidx.compose.foundation.isSystemInDarkTheme()) Color.White.copy(alpha = 0.7f) else Color(0xFF1E3A8A),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 64.dp)
            )
        }
    }
}
