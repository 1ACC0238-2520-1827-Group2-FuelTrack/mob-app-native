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
                loginResponse.userId?.let { userId ->
                    preferencesManager.saveUserId(userId)
                }
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
                preferencesManager.saveToken(registerResponse.token)
                registerResponse.userId?.let { userId ->
                    preferencesManager.saveUserId(userId)
                }
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
}

