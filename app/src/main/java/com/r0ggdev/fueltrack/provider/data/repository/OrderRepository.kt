package com.r0ggdev.fueltrack.provider.data.repository

import com.r0ggdev.fueltrack.provider.data.dto.OrderDto
import com.r0ggdev.fueltrack.provider.data.dto.UpdateOrderStatusRequest
import com.r0ggdev.fueltrack.provider.data.remote.ProviderApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val apiService: ProviderApiService
) {
    suspend fun getProviderOrders(): Result<List<OrderDto>> {
        return try {
            val response = apiService.getProviderOrders()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener órdenes: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOrderById(id: String): Result<OrderDto> {
        return try {
            val response = apiService.getOrderById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener orden: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateOrderStatus(id: String, status: Int): Result<Unit> {
        return try {
            val response = apiService.updateOrderStatus(id, UpdateOrderStatusRequest(status))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al actualizar estado: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

