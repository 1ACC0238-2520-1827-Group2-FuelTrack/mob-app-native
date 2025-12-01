package com.r0ggdev.fueltrack.data.repository

import com.r0ggdev.fueltrack.data.dto.CreateExpenseRequest
import com.r0ggdev.fueltrack.data.dto.ExpenseDto
import com.r0ggdev.fueltrack.data.dto.UpdateExpenseRequest
import com.r0ggdev.fueltrack.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getExpensesByVehicle(vehicleId: String): Result<List<ExpenseDto>> {
        return try {
            val response = apiService.getExpensesByVehicle(vehicleId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener gastos: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createExpense(request: CreateExpenseRequest): Result<ExpenseDto> {
        return try {
            val response = apiService.createExpense(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear gasto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateExpense(id: String, request: UpdateExpenseRequest): Result<ExpenseDto> {
        return try {
            val response = apiService.updateExpense(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar gasto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteExpense(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteExpense(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar gasto: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

