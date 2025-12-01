package com.r0ggdev.fueltrack.provider.data.repository

import com.r0ggdev.fueltrack.provider.data.dto.CreateOperatorRequest
import com.r0ggdev.fueltrack.provider.data.dto.OperatorDto
import com.r0ggdev.fueltrack.provider.data.dto.UpdateOperatorRequest
import com.r0ggdev.fueltrack.provider.data.remote.ProviderApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OperatorRepository @Inject constructor(
    private val apiService: ProviderApiService
) {
    suspend fun getOperators(): Result<List<OperatorDto>> {
        return try {
            val response = apiService.getOperators()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener operadores: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getOperatorById(id: String): Result<OperatorDto> {
        return try {
            val response = apiService.getOperatorById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al obtener operador: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createOperator(request: CreateOperatorRequest): Result<OperatorDto> {
        return try {
            val response = apiService.createOperator(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al crear operador: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateOperator(id: String, request: UpdateOperatorRequest): Result<OperatorDto> {
        return try {
            val response = apiService.updateOperator(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al actualizar operador: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteOperator(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteOperator(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: response.message()
                Result.failure(Exception("Error al eliminar operador: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

