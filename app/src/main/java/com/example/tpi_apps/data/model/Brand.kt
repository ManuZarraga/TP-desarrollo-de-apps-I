package com.example.tpi_apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    val id: String? = null,
    val name: String,
    @SerialName("image_res")
    val imageRes: String? = null,
    @SerialName("background_color")
    val backgroundColor: String? = null
)
