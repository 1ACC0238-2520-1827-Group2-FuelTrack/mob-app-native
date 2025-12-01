package com.r0ggdev.fueltrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val userId: String? = null,

    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val role: String? = null
)


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            combine(
                authRepository.getToken(),
                authRepository.getUserId(),
                authRepository.getUserRole()
            ) { token, userId, role ->
                println("DEBUG: AuthViewModel - checkAuthStatus:")
                println("DEBUG: AuthViewModel - token: ${token?.take(20)}...")
                println("DEBUG: AuthViewModel - userId: $userId")
                println("DEBUG: AuthViewModel - role: '$role'")
                println("DEBUG: AuthViewModel - role?.uppercase(): '${role?.uppercase()}'")
                println("DEBUG: AuthViewModel - role == 'PROVIDER': ${role == "PROVIDER"}")
                println("DEBUG: AuthViewModel - role?.uppercase() == 'PROVIDER': ${role?.uppercase() == "PROVIDER"}")
                println("DEBUG: AuthViewModel - isAuthenticated: ${token != null}")

                _uiState.value = _uiState.value.copy(
                    isAuthenticated = token != null,
                    token = token,
                    userId = userId,
                    role = role
                )
            }.collect { }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authRepository.login(email, password)
                .onSuccess { response ->

                    println("DEBUG: AuthViewModel - Login SUCCESS")
                    println("DEBUG: AuthViewModel - User role from API: '${response.user.role}'")
                    println("DEBUG: AuthViewModel - User role uppercase: '${response.user.role.uppercase()}'")
                    println("DEBUG: AuthViewModel - User role == 'PROVIDER': ${response.user.role == "PROVIDER"}")
                    println("DEBUG: AuthViewModel - User role uppercase == 'PROVIDER': ${response.user.role.uppercase() == "PROVIDER"}")
                    println("DEBUG: AuthViewModel - User role contains 'PROVIDER': ${response.user.role.contains("PROVIDER", ignoreCase = true)}")
                    println("DEBUG: AuthViewModel - User role contains 'ADMIN': ${response.user.role.contains("ADMIN", ignoreCase = true)}")

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,

                        token = response.accessToken,
                        userId = response.user.id.toString(),

                        firstName = response.user.firstName,
                        lastName = response.user.lastName,
                        email = response.user.email,
                        role = response.user.role,
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }


    fun register(firstName: String, lastName: String, email: String, password: String, phone: String = "") {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            authRepository.register(firstName, lastName, email, password, phone)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        token = it.accessToken,
                        userId = it.user.id.toString(),

                        // 🔵 guardamos datos del usuario registrado
                        firstName = it.user.firstName,
                        lastName = it.user.lastName,
                        email = it.user.email,
                        role = it.user.role,
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _uiState.value = AuthUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
