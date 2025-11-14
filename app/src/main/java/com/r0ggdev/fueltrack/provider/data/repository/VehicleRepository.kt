package com.r0ggdev.fueltrack.provider.data.repository

import com.r0ggdev.fueltrack.provider.data.dto.ProviderVehicleDto
import com.r0ggdev.fueltrack.provider.data.dto.UpdateVehicleLocationRequest
import com.r0ggdev.fueltrack.provider.data.remote.ProviderApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepository @Inject constructor(
    private val apiService: ProviderApiService
) {
    suspend fun getVehicles(): Result<List<ProviderVehicleDto>> {
        return try {
            val response = apiService.getVehicles()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener vehículos: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getVehicleById(id: String): Result<ProviderVehicleDto> {
        return try {
            val response = apiService.getVehicleById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener vehículo: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateVehicleLocation(id: String, latitude: Double, longitude: Double): Result<Unit> {
        return try {
            val response = apiService.updateVehicleLocation(
                id,
                UpdateVehicleLocationRequest(latitude, longitude)
            )
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al actualizar ubicación: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

