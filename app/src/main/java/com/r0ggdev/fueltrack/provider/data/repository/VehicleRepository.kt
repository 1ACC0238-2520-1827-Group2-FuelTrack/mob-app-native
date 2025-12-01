package com.r0ggdev.fueltrack.provider.data.repository

import com.r0ggdev.fueltrack.provider.data.dto.CreateVehicleRequest
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
            println("DEBUG: VehicleRepository - Calling apiService.getVehicles() - URL should be: api/Vehicles")
            val response = apiService.getVehicles()
            println("DEBUG: VehicleRepository - Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")
            if (response.isSuccessful && response.body() != null) {
                val vehicles = response.body()!!
                println("DEBUG: VehicleRepository - Success! Vehicles count: ${vehicles.size}")
                vehicles.forEach { vehicle ->
                    println("DEBUG: Vehicle - ${vehicle.licensePlate}: ${vehicle.brand} ${vehicle.model}")
                }
                Result.success(vehicles)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                println("DEBUG: VehicleRepository - Error: $errorBody (code: ${response.code()})")
                Result.failure(Exception("Error al obtener vehículos: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            println("DEBUG: VehicleRepository - Exception: ${e.message}")
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

    suspend fun createVehicle(request: CreateVehicleRequest): Result<ProviderVehicleDto> {
        return try {
            println("DEBUG: VehicleRepository - ==========================================")
            println("DEBUG: VehicleRepository - CREATING VEHICLE: ${request.licensePlate}")
            println("DEBUG: VehicleRepository - IMPORTANT: This endpoint requires PROVIDER permissions!")
            println("DEBUG: VehicleRepository - If you get 403 Forbidden, check:")
            println("DEBUG: VehicleRepository -   1. User has PROVIDER role in backend")
            println("DEBUG: VehicleRepository -   2. PROVIDER role has vehicle creation permissions")
            println("DEBUG: VehicleRepository -   3. Endpoint /api/Vehicles POST allows PROVIDER role")
            println("DEBUG: VehicleRepository - ==========================================")

            val response = apiService.createVehicle(request)

            // Log request details
            response.raw().request.let { request ->
                println("DEBUG: VehicleRepository - REQUEST DETAILS:")
                println("DEBUG: VehicleRepository - URL: ${request.url}")
                println("DEBUG: VehicleRepository - Method: ${request.method}")
                val authHeader = request.headers.find { it.first == "Authorization" }
                if (authHeader != null) {
                    println("DEBUG: VehicleRepository - Auth: ${authHeader.second.take(30)}...")
                } else {
                    println("DEBUG: VehicleRepository - ❌ NO AUTH HEADER!")
                }
            }

            println("DEBUG: VehicleRepository - RESPONSE:")
            println("DEBUG: VehicleRepository - Code: ${response.code()}, Success: ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val createdVehicle = response.body()!!
                println("DEBUG: VehicleRepository - ✅ SUCCESS! Vehicle created: ${createdVehicle.licensePlate}")
                Result.success(createdVehicle)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                println("DEBUG: VehicleRepository - ❌ FAILED!")
                println("DEBUG: VehicleRepository - Error body: '$errorBody'")

                when (response.code()) {
                    401 -> println("DEBUG: VehicleRepository - 401 UNAUTHORIZED - Check authentication")
                    403 -> println("DEBUG: VehicleRepository - 403 FORBIDDEN - PROVIDER role lacks permissions!")
                    400 -> println("DEBUG: VehicleRepository - 400 BAD REQUEST - Check request data")
                    500 -> println("DEBUG: VehicleRepository - 500 SERVER ERROR - Backend issue")
                    else -> println("DEBUG: VehicleRepository - ${response.code()} - Unknown error")
                }

                Result.failure(Exception("Error al crear vehículo: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            println("DEBUG: VehicleRepository - 💥 EXCEPTION: ${e.message}")
            Result.failure(e)
        }
    }
}

