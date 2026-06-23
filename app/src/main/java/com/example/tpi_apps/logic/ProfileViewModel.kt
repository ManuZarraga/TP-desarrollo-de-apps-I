package com.example.tpi_apps.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.dto.ProfileUpdateRequest
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _isUpdating = MutableStateFlow(false)
    val isUpdating: StateFlow<Boolean> = _isUpdating

    fun logout() {
        viewModelScope.launch {
            try {
                SupabaseModule.client.auth.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateProfile(userId: String, name: String, username: String, email: String, avatarSeed: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                val updates = ProfileUpdateRequest(
                    name = name,
                    username = username,
                    avatarSeed = avatarSeed
                )
                try {
                    SupabaseModule.apiService.updateProfile(mapOf("id" to "eq.$userId"), updates)
                } catch (e: Exception) {
                    SupabaseModule.apiService.updateProfile(mapOf("email" to "eq.$email"), updates)
                }
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            } finally {
                _isUpdating.value = false
            }
        }
    }

    fun redeemReward(userId: String, email: String, currentPoints: Int, cost: Int, onResult: (Boolean) -> Unit) {
        if (currentPoints < cost) {
            onResult(false)
            return
        }
        viewModelScope.launch {
            _isUpdating.value = true
            try {
                val newPoints = currentPoints - cost
                val updates = ProfileUpdateRequest(points = newPoints)
                
                try {
                    SupabaseModule.apiService.updateProfile(mapOf("id" to "eq.$userId"), updates)
                } catch (e: Exception) {
                    SupabaseModule.apiService.updateProfile(mapOf("email" to "eq.$email"), updates)
                }

                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            } finally {
                _isUpdating.value = false
            }
        }
    }
}
