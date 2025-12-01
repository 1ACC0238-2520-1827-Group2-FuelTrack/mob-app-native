package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.DashboardDto
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getDashboard(vehicleId: String): Result<DashboardDto> {
        return try {
            val response = apiService.getDashboard(vehicleId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener dashboard: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

