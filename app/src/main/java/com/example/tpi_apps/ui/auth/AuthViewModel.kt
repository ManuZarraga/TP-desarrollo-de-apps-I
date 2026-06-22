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
                // 1. Validar si el username ya existe
                // Usamos eq. para el filtro de PostgREST
                val userCheck = SupabaseModule.apiService.getProfileByUsername("eq.$username")
                if (userCheck.isNotEmpty()) {
                    _uiState.value = AuthState.Error("El nombre de usuario ya está en uso.")
                    return@launch
                }

                // 2. Registro en Supabase Auth
                SupabaseModule.client.auth.signUpWith(Email) {
                    this.email = email
                    password = pass
                }
                
                // 2. Obtener el ID real generado por Supabase Auth
                // Tras el signUp, si no hay confirmación de email, el usuario suele estar ya en la sesión local
                // o podemos obtenerlo del cliente.
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
                    
                    // 3. Crear el perfil en la tabla pública vinculando el UID
                    try {
                        SupabaseModule.apiService.createProfile(newUser)
                        _uiState.value = AuthState.Success("¡Cuenta creada con éxito! Revisa tu email para confirmar.")
                    } catch (e: Exception) {
                        // Si falla la creación del perfil pero el usuario de Auth existe
                        println("Error creando perfil en DB: ${e.message}")
                        _uiState.value = AuthState.Error("Se creó el usuario pero hubo un problema con el perfil.")
                    }
                } else {
                    // Si el email confirmation está activado y el SDK no devuelve el user inmediatamente
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
                SupabaseModule.client.auth.signInWith(Email) {
                    this.email = email
                    password = pass
                }
                _uiState.value = AuthState.Authenticated
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = AuthState.Error(e.localizedMessage ?: "Error al iniciar sesión")
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
