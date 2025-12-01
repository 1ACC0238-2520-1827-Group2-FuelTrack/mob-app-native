package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.CreateVehicleRequest
import com.r0ggdev.fueltrack.data.dto.UpdateVehicleRequest
import com.r0ggdev.fueltrack.data.dto.VehicleDto
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getVehicles(): Result<List<VehicleDto>> {
        return try {
            val response = apiService.getVehicles()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener vehículos: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createVehicle(request: CreateVehicleRequest): Result<VehicleDto> {
        return try {
            val response = apiService.createVehicle(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear vehículo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateVehicle(id: String, request: UpdateVehicleRequest): Result<VehicleDto> {
        return try {
            val response = apiService.updateVehicle(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar vehículo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteVehicle(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteVehicle(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar vehículo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVehicle(id: String): Result<VehicleDto> {
        return try {
            val response = apiService.getVehicleById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener vehículo: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}

