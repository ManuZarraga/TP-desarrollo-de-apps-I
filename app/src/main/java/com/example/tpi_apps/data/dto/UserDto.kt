package com.example.tpi_apps.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    @SerialName("name") val name: String? = null,
    @SerialName("username") val username: String? = null,
    @SerialName("avatar_seed") val avatarSeed: String? = null,
    @SerialName("points") val points: Int? = null,
    @SerialName("level") val level: String? = null,
    @SerialName("reputation") val reputation: Double? = null,
    @SerialName("review_count") val reviewCount: Int? = null
)
