package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.LoginRequest
import com.r0ggdev.fueltrack.data.dto.LoginResponse
import com.r0ggdev.fueltrack.data.dto.RegisterRequest
import com.r0ggdev.fueltrack.data.dto.RegisterResponse
import com.r0ggdev.fueltrack.data.local.PreferencesManager
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

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
                preferencesManager.saveToken(loginResponse.token)
                preferencesManager.saveUserId(loginResponse.userId)
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Error en el login: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun register(nombre: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(nombre, email, password))
            if (response.isSuccessful && response.body() != null) {
                val registerResponse = response.body()!!
                preferencesManager.saveToken(registerResponse.token)
                preferencesManager.saveUserId(registerResponse.userId)
                Result.success(registerResponse)
            } else {
                Result.failure(Exception("Error en el registro: ${response.message()}"))
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
}

