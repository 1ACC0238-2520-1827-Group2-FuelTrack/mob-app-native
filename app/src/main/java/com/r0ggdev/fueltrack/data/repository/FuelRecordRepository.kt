package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.CreateFuelRecordRequest
import com.r0ggdev.fueltrack.data.dto.FuelRecordDto
import com.r0ggdev.fueltrack.data.dto.UpdateFuelRecordRequest
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FuelRecordRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getFuelRecordsByVehicle(vehicleId: String): Result<List<FuelRecordDto>> {
        return try {
            val response = apiService.getFuelRecordsByVehicle(vehicleId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener registros: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getLatestFuelRecord(vehicleId: String): Result<FuelRecordDto?> {
        return try {
            val response = apiService.getLatestFuelRecord(vehicleId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error al obtener último registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createFuelRecord(request: CreateFuelRecordRequest): Result<FuelRecordDto> {
        return try {
            val response = apiService.createFuelRecord(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateFuelRecord(id: String, request: UpdateFuelRecordRequest): Result<FuelRecordDto> {
        return try {
            val response = apiService.updateFuelRecord(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteFuelRecord(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteFuelRecord(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar registro: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

