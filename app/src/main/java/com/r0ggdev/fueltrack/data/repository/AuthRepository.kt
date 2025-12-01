package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.LoginRequest
import com.r0ggdev.fueltrack.data.dto.LoginResponse
import com.r0ggdev.fueltrack.data.dto.RegisterRequest
import com.r0ggdev.fueltrack.data.dto.RegisterResponse
import com.r0ggdev.fueltrack.data.local.PreferencesManager
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                preferencesManager.saveToken(loginResponse.accessToken)
                preferencesManager.saveUserId(loginResponse.user.id.toString())
                preferencesManager.saveUserRole(loginResponse.user.role)

                // Debug: verificar que se guardó correctamente
                println("DEBUG: AuthRepository - After saving login data:")
                println("DEBUG: AuthRepository - Saved role: '${loginResponse.user.role}'")
                preferencesManager.debugStoredValues()

                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error en el login: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(firstName: String, lastName: String, email: String, password: String, phone: String = ""): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                phone = phone,
                role = 1
            ))
            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!
                preferencesManager.saveToken(registerResponse.accessToken)
                preferencesManager.saveUserId(registerResponse.user.id.toString())
                preferencesManager.saveUserRole(registerResponse.user.role)
                Result.success(registerResponse)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error en el registro: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        preferencesManager.clear()
    }

    fun getToken() = preferencesManager.token
    fun getUserId() = preferencesManager.userId
    fun getUserRole() = preferencesManager.userRole

    suspend fun getCurrentToken(): String? {
        return preferencesManager.token.first()
    }

}

