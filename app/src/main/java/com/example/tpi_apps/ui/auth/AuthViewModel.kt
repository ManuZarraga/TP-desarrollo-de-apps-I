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
                val userCheck = SupabaseModule.apiService.getProfileByUsername("eq.$username")
                if (userCheck.isNotEmpty()) {
                    _uiState.value = AuthState.Error("El nombre de usuario ya está en uso.")
                    return@launch
                }

                SupabaseModule.client.auth.signUpWith(Email) {
                    this.email = email
                    password = pass
                }
                

                val userId = SupabaseModule.client.auth.currentUserOrNull()?.id
                
                if (userId != null) {
                    val newUser = User(
                        id = userId,
                        name = name,
                        username = username,
                        email = email,
                        avatarSeed = "hamburger",
                        points = 0,
                        level = "Bronce",
                        reputation = 0.0,
                        reviewCount = 0
                    )

                    try {
                        SupabaseModule.apiService.createProfile(newUser)
                        _uiState.value = AuthState.Success("¡Cuenta creada con éxito! Revisa tu email para confirmar.")
                    } catch (e: Exception) {
                        println("Error creando perfil en DB: ${e.message}")
                        _uiState.value = AuthState.Error("Se creó el usuario pero hubo un problema con el perfil.")
                    }
                } else {
                    _uiState.value = AuthState.Success("Registro iniciado. Por favor, confirma tu email para activar tu cuenta.")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = AuthState.Error(e.localizedMessage ?: "Error al registrarse")
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthState.Loading
            try {
                try {
                    SupabaseModule.client.auth.signInWith(Email) {
                        this.email = email
                        password = pass
                    }
                    _uiState.value = AuthState.Authenticated
                } catch (e: Exception) {
                    try {
                        val emailExists = SupabaseModule.apiService.getProfileByEmail("eq.$email")
                        if (emailExists.isEmpty()) {
                            _uiState.value = AuthState.Error("No existe cuenta asociada al mail.")
                        } else {
                            _uiState.value = AuthState.Error("Contraseña incorrecta.")
                        }
                    } catch (dbError: Exception) {
                        _uiState.value = AuthState.Error("Credenciales inválidas o error de conexión.")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = AuthState.Error("Error inesperado. Inténtalo de nuevo.")
            }
        }
    }

    fun getCurrentUserId(): String? {
        return SupabaseModule.client.auth.currentUserOrNull()?.id
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object Authenticated : AuthState()
}
