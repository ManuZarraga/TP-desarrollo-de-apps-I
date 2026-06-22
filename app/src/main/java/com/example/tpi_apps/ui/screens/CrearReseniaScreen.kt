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
import androidx.compose.material.icons.filled.Close
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
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReseniaScreen(
    user: User,
    navController: NavController,
    onSettingsClick: () -> Unit,
    onReviewsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrearReseniaViewModel = viewModel(),
    initialImageUri: Uri? = null
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
    var selectedImageUris by remember { mutableStateOf(listOfNotNull(initialImageUri)) }
    var currentSlotIndex by remember { mutableIntStateOf(0) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            val newList = selectedImageUris.toMutableList()
            if (currentSlotIndex < newList.size) {
                newList[currentSlotIndex] = tempPhotoUri!!
            } else {
                newList.add(tempPhotoUri!!)
            }
            selectedImageUris = newList.take(3)
        }
    }

    fun launchCamera(index: Int) {
        currentSlotIndex = index
        val file = File(context.externalCacheDir, "review_photo_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        tempPhotoUri = uri
        cameraLauncher.launch(uri)
    }

    val filteredFoods = foods.filter { it.brandId == selectedBrandId }

    var expandedRest by remember { mutableStateOf(false) }
    var expandedPedido by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background)
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
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedTextColor = MaterialTheme.colorScheme.primary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.primary
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                        focusedTextColor = MaterialTheme.colorScheme.primary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.primary
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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(100.dp)
                            )
                            Row {
                                repeat(5) { index ->
                                    Icon(
                                        painter = painterResource(
                                            id = if (index < puntuacion) R.drawable.star_selected else R.drawable.star_unselected
                                        ),
                                        contentDescription = null,
                                        tint = if (index < puntuacion) MaterialTheme.colorScheme.primary else Color.Unspecified,
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedTextField(
                            value = comentario,
                            onValueChange = { comentario = it },
                            placeholder = {
                                Text(
                                    "Da un descripción de tu producto, si cumplió con tus expectativas o si llegó igual que en la publicidad...",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            shape = RoundedCornerShape(8.dp),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.primary),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedTextColor = MaterialTheme.colorScheme.primary,
                                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Slot Principal
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        launchCamera(0)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImageUris.isNotEmpty()) {
                                    AsyncImage(
                                        model = selectedImageUris[0],
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    // Botón para eliminar
                                    IconButton(
                                        onClick = {
                                            selectedImageUris = selectedImageUris.toMutableList().apply { removeAt(0) }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(24.dp)
                                            .padding(4.dp)
                                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                } else {
                                    Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Slot 2
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(71.dp)
                                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            launchCamera(1)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selectedImageUris.size > 1) {
                                        AsyncImage(
                                            model = selectedImageUris[1],
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        // Botón para eliminar
                                        IconButton(
                                            onClick = {
                                                selectedImageUris = selectedImageUris.toMutableList().apply { removeAt(1) }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(24.dp)
                                                .padding(4.dp)
                                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    } else {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                                // Slot 3
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(71.dp)
                                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            launchCamera(2)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (selectedImageUris.size > 2) {
                                        AsyncImage(
                                            model = selectedImageUris[2],
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        // Botón para eliminar
                                        IconButton(
                                            onClick = {
                                                selectedImageUris = selectedImageUris.toMutableList().apply { removeAt(2) }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(24.dp)
                                                .padding(4.dp)
                                                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                    } else {
                                        Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { 
                                launchCamera(selectedImageUris.size.coerceAtMost(2))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Tomar una nueva foto", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                if (selectedBrandId != null && selectedFoodId != null && puntuacion > 0) {
                                    coroutineScope.launch {
                                        val uploadedUrls = mutableListOf<String>()
                                        for (uri in selectedImageUris) {
                                            val inputStream = context.contentResolver.openInputStream(uri)
                                            val bytes = inputStream?.readBytes()
                                            inputStream?.close()
                                            if (bytes != null) {
                                                val url = viewModel.uploadImage(bytes)
                                                if (url != null) {
                                                    uploadedUrls.add(url)
                                                }
                                            }
                                        }

                                        viewModel.submitReview(
                                            user = user,
                                            brandId = selectedBrandId!!,
                                            foodId = selectedFoodId!!,
                                            rating = puntuacion,
                                            comment = comentario,
                                            images = if (uploadedUrls.isEmpty()) null else uploadedUrls
                                        )
                                        navController.navigate(Routes.Confirmacion.route)
                                    }
                                }
                            },
                            enabled = !uploading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            if (uploading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Subir Reseña", color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
