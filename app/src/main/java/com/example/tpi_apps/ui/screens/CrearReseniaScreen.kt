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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import java.io.InputStream

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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val brands by viewModel.brands.collectAsState()
    val foods by viewModel.foods.collectAsState()
    val uploading by viewModel.uploading.collectAsState()

    var selectedBrandId by remember { mutableStateOf<String?>(null) }
    var restaurante by remember { mutableStateOf("") }
    var selectedFoodId by remember { mutableStateOf<String?>(null) }
    var pedido by remember { mutableStateOf("") }
    var puntuacion by remember { mutableIntStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val filteredFoods = foods.filter { it.brandId == selectedBrandId }

    var expandedRest by remember { mutableStateOf(false) }
    var expandedPedido by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Restaurante:",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
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
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF3A63ED)),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF3A63ED),
                                        unfocusedBorderColor = Color(0xFFCBD5E1),
                                        focusedTextColor = Color(0xFF3A63ED),
                                        unfocusedTextColor = Color(0xFF3A63ED)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedRest,
                                    onDismissRequest = { expandedRest = false }
                                ) {
                                    brands.forEach { brand ->
                                        DropdownMenuItem(
                                            text = { Text(brand.name) },
                                            onClick = {
                                                restaurante = brand.name
                                                selectedBrandId = brand.id
                                                pedido = "" // Reset food selection when brand changes
                                                selectedFoodId = null
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
                                color = Color(0xFF64748B),
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
                                    textStyle = LocalTextStyle.current.copy(color = Color(0xFF3A63ED)),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF3A63ED),
                                        unfocusedBorderColor = Color(0xFFCBD5E1),
                                        focusedTextColor = Color(0xFF3A63ED),
                                        unfocusedTextColor = Color(0xFF3A63ED)
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedPedido,
                                    onDismissRequest = { expandedPedido = false }
                                ) {
                                    filteredFoods.forEach { food ->
                                        DropdownMenuItem(
                                            text = { Text(food.name) },
                                            onClick = {
                                                pedido = food.name
                                                selectedFoodId = food.id
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
                                color = Color(0xFF64748B),
                                modifier = Modifier.width(100.dp)
                            )
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        painter = painterResource(
                                            id = if (index < puntuacion) R.drawable.star_selected else R.drawable.star_unselected
                                        ),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable { puntuacion = index + 1 }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Escribe una reseña:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
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
                            textStyle = LocalTextStyle.current.copy(color = Color(0xFF3A63ED)),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3A63ED),
                                unfocusedBorderColor = Color(0xFFCBD5E1),
                                focusedTextColor = Color(0xFF3A63ED),
                                unfocusedTextColor = Color(0xFF3A63ED),
                                cursorColor = Color(0xFF3A63ED)
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImageUri != null) {
                                    AsyncImage(
                                        model = selectedImageUri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.Gray)
                                }
                            }
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
                            onClick = { launcher.launch("image/*") },
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
                                if (selectedBrandId != null && selectedFoodId != null && puntuacion > 0) {
                                    coroutineScope.launch {
                                        var uploadedUrl: String? = null
                                        selectedImageUri?.let { uri ->
                                            val inputStream = context.contentResolver.openInputStream(uri)
                                            val bytes = inputStream?.readBytes()
                                            inputStream?.close()
                                            if (bytes != null) {
                                                uploadedUrl = viewModel.uploadImage(bytes)
                                            }
                                        }

                                        viewModel.submitReview(
                                            user = user,
                                            brandId = selectedBrandId!!,
                                            foodId = selectedFoodId!!,
                                            rating = puntuacion,
                                            comment = comentario,
                                            imageUrl = uploadedUrl
                                        )
                                        navController.navigate(Routes.Confirmacion.route)
                                    }
                                }
                            },
                            enabled = !uploading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A63ED))
                        ) {
                            if (uploading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Subir Reseña", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
