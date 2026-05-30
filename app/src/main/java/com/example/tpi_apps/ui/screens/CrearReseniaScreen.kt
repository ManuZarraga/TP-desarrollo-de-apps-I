package com.example.tpi_apps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.navigation.NavController
import com.example.tpi_apps.R
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.logic.CrearReseniaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tpi_apps.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReseniaScreen(
    user: User,
    navController: NavController,
    onSettingsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrearReseniaViewModel = viewModel()
) {
    var restaurante by remember { mutableStateOf("") }
    var pedido by remember { mutableStateOf("") }
    var puntuacion by remember { mutableIntStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    val restaurantes = listOf("Kentucky", "McDonald's", "Burger King", "Mostaza")
    val pedidos = listOf("Pizza Margarita", "Big Mac", "Whopper", "Mega Stack")

    var expandedRest by remember { mutableStateOf(false) }
    var expandedPedido by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A63ED), Color(0xFFF8FAFC)),
                    startY = 0f,
                    endY = 800f
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.hero_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillBounds,
            alpha = 1f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Restaurante:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(100.dp)
                            )
                            ExposedDropdownMenuBox(
                                expanded = expandedRest,
                                onExpandedChange = { expandedRest = !expandedRest }
                            ) {
                                OutlinedTextField(
                                    value = restaurante,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Selecciona un restaurante...", fontSize = 12.sp) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRest) },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedRest,
                                    onDismissRequest = { expandedRest = false }
                                ) {
                                    restaurantes.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                restaurante = selectionOption
                                                expandedRest = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pedido",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(100.dp)
                            )
                            ExposedDropdownMenuBox(
                                expanded = expandedPedido,
                                onExpandedChange = { expandedPedido = !expandedPedido }
                            ) {
                                OutlinedTextField(
                                    value = pedido,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("Selecciona lo que pediste...", fontSize = 12.sp) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPedido) },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedPedido,
                                    onDismissRequest = { expandedPedido = false }
                                ) {
                                    pedidos.forEach { selectionOption ->
                                        DropdownMenuItem(
                                            text = { Text(selectionOption) },
                                            onClick = {
                                                pedido = selectionOption
                                                expandedPedido = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Puntuación:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(100.dp)
                            )
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = if (index < puntuacion) Icons.Filled.Star else Icons.Filled.StarBorder,
                                        contentDescription = null,
                                        tint = if (index < puntuacion) Color(0xFF6366F1) else Color.LightGray,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable { puntuacion = index + 1 }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "Escribe una reseña:", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = comentario,
                            onValueChange = { comentario = it },
                            placeholder = {
                                Text(
                                    "Da un descripción de tu producto, si cumplió con tus expectativas o si llegó igual que en la publicidad...",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.LightGray
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.review_card_burger),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(71.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1E3A8A))
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(71.dp)
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1E3A8A))
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A63ED).copy(alpha = 0.54f))
                        ) {
                            Text("Agregar una nueva foto", color = Color.White)
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (restaurante.isNotEmpty() && pedido.isNotEmpty() && puntuacion > 0) {
                                    viewModel.submitReview(
                                        user = user,
                                        restaurantName = restaurante,
                                        itemName = pedido,
                                        rating = puntuacion,
                                        comment = comentario
                                    )
                                    navController.navigate(Routes.Confirmacion.route)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A63ED))
                        ) {
                            Text("Subir Reseña", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
