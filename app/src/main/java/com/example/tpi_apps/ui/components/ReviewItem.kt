package com.example.tpi_apps.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tpi_apps.R
import com.example.tpi_apps.data.model.Review

@Composable
fun ReviewItem(
    review: Review,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(280.dp)
            .padding(start = 16.dp, end = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            val imageRes = when {
                review.itemName.contains("Big Mac", ignoreCase = true) -> R.drawable.review_card_bigmac
                review.itemName.contains("Pizza", ignoreCase = true) -> R.drawable.review_card_pizza
                else -> R.drawable.review_card_pastas
            }

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "${review.restaurantName}: ${review.itemName}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Stars
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        val isSelected = index < review.rating
                        Icon(
                            painter = painterResource(
                                id = if (isSelected) R.drawable.star_selected else R.drawable.star_unselected
                            ),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = review.date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${review.time} PM",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "\"${review.comment}\"",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp,
                        color = Color.Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
