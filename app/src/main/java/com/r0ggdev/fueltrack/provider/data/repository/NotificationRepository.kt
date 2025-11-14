package com.r0ggdev.fueltrack.provider.data.repository

import com.r0ggdev.fueltrack.provider.data.dto.NotificationDto
import com.r0ggdev.fueltrack.provider.data.remote.ProviderApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ProviderApiService
) {
    suspend fun getNotifications(): Result<List<NotificationDto>> {
        return try {
            val response = apiService.getNotifications()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener notificaciones: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getNotificationById(id: String): Result<NotificationDto> {
        return try {
            val response = apiService.getNotificationById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener notificación: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun markNotificationAsRead(id: String): Result<Unit> {
        return try {
            val response = apiService.markNotificationAsRead(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al marcar como leído: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

