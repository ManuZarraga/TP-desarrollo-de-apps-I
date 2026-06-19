package com.example.tpi_apps.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tpi_apps.data.model.User
import com.example.tpi_apps.data.network.SupabaseModule
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    fun signUp(email: String, pass: String, name: String, username: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            try {
                val authUser = SupabaseModule.client.auth.signUpWith(Email) {
                    this.email = email
                    password = pass
                }
                
                // Si el usuario se creó en Auth, creamos el perfil en la tabla profiles
                val userId = SupabaseModule.client.auth.currentUserOrNull()?.id
                if (userId != null) {
                    val newUser = User(
                        id = userId,
                        name = name,
                        username = username,
                        email = email,
                        avatarSeed = username.replace(" ", "_").lowercase(),
                        points = 0,
                        level = "Bronce",
                        reputation = 0.0,
                        reviewCount = 0
                    )
                    try {
                        SupabaseModule.apiService.createProfile(newUser)
                    } catch (e: Exception) {
                        println("Error creando perfil: ${e.message}")
                    }
                }

                _uiState.value = AuthState.Success("Usuario creado correctamente. Revisa tu email para confirmar.")
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.localizedMessage ?: "Error al registrarse")
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            try {
                SupabaseModule.client.auth.signInWith(Email) {
                    this.email = email
                    password = pass
                }
                _uiState.value = AuthState.Authenticated
            } catch (e: Exception) {
                _uiState.value = AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object Authenticated : AuthState()
}
