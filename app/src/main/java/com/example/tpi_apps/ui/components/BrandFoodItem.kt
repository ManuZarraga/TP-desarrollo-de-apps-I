package com.example.tpi_apps.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.tpi_apps.data.model.Food

@Composable
fun BrandFoodItem(
    food: Food,
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf(false) }
    
    val mainImageRes = when {
        food.name.contains("Big Mac", ignoreCase = true) || food.category.contains("Hamburguesa", ignoreCase = true) -> R.drawable.review_card_bigmac
        food.category.contains("Pizza", ignoreCase = true) -> R.drawable.review_card_pizza
        food.category.contains("Sushi", ignoreCase = true) -> R.drawable.review_card_sushi
        food.category.contains("Pasta", ignoreCase = true) -> R.drawable.review_card_pastas
        food.category.contains("Shawarma", ignoreCase = true) -> R.drawable.review_card_shawarma
        food.category.contains("Postres", ignoreCase = true) -> R.drawable.review_card_postre
        else -> R.drawable.review_card_pastas
    }
    
    val subImage1 = when {
        food.category.contains("Sushi", ignoreCase = true) -> R.drawable.review_card_sushi2
        food.category.contains("Pasta", ignoreCase = true) -> R.drawable.review_card_pastas2
        food.category.contains("Postres", ignoreCase = true) -> R.drawable.review_card_franui
        else -> mainImageRes
    }
    
    val subImage2 = when {
        food.category.contains("Hamburguesa", ignoreCase = true) -> R.drawable.food_cell1
        food.category.contains("Pizza", ignoreCase = true) -> R.drawable.food_cell2
        else -> mainImageRes
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = food.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_selected),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${food.rating} · ${food.restaurant} · $$$",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
                
                Icon(
                    painter = painterResource(
                        id = if (isLiked) R.drawable.heart_selected else R.drawable.heart_unselected
                    ),
                    contentDescription = "Like",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { isLiked = !isLiked }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = mainImageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = subImage1),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = painterResource(id = subImage2),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
