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
            println("DEBUG: OrderRepository - ==========================================")
            println("DEBUG: OrderRepository - CALLING API: getProviderOrders()")
            println("DEBUG: OrderRepository - Expected URL: https://fueltrack-api.onrender.com/api/Orders/provider")
            println("DEBUG: OrderRepository - IMPORTANT: This endpoint requires PROVIDER role!")
            println("DEBUG: OrderRepository - 403 Forbidden usually means:")
            println("DEBUG: OrderRepository -   - User is authenticated but doesn't have PROVIDER role")
            println("DEBUG: OrderRepository -   - User role is stored incorrectly in app")
            println("DEBUG: OrderRepository -   - Backend expects different role name")
            println("DEBUG: OrderRepository - ==========================================")

            val response = apiService.getProviderOrders()

            // Log request details first
            response.raw().request.let { request ->
                println("DEBUG: OrderRepository - REQUEST DETAILS:")
                println("DEBUG: OrderRepository - URL: ${request.url}")
                println("DEBUG: OrderRepository - Method: ${request.method}")
                val authHeader = request.headers.find { it.first == "Authorization" }
                if (authHeader != null) {
                    println("DEBUG: OrderRepository - Auth Header: ${authHeader.second.take(30)}...")
                    // Check if token looks valid (should start with "Bearer ")
                    if (authHeader.second.startsWith("Bearer ")) {
                        println("DEBUG: OrderRepository - ✅ Token format looks valid")
                        // Try to decode token payload (basic check)
                        try {
                            val tokenParts = authHeader.second.split(".")
                            if (tokenParts.size == 3) {
                                println("DEBUG: OrderRepository - ✅ JWT token structure is valid")
                            } else {
                                println("DEBUG: OrderRepository - ⚠️  JWT token structure is invalid")
                            }
                        } catch (e: Exception) {
                            println("DEBUG: OrderRepository - ⚠️  Could not parse token structure")
                        }
                    } else {
                        println("DEBUG: OrderRepository - ❌ Token does not start with 'Bearer '")
                    }
                } else {
                    println("DEBUG: OrderRepository - ❌ NO AUTH HEADER - User not logged in!")
                }

                // Check all headers
                println("DEBUG: OrderRepository - All Headers:")
                request.headers.forEach { header ->
                    if (header.first.lowercase().contains("auth")) {
                        println("DEBUG: OrderRepository -   ${header.first}: ${header.second.take(30)}...")
                    } else {
                        println("DEBUG: OrderRepository -   ${header.first}: ${header.second}")
                    }
                }
            }

            println("DEBUG: OrderRepository - RESPONSE DETAILS:")
            println("DEBUG: OrderRepository - Status Code: ${response.code()}")
            println("DEBUG: OrderRepository - isSuccessful: ${response.isSuccessful}")
            println("DEBUG: OrderRepository - Message: '${response.message()}'")

            if (response.isSuccessful && response.body() != null) {
                val orders = response.body()!!
                println("DEBUG: OrderRepository - ✅ SUCCESS! Orders count: ${orders.size}")
                if (orders.isNotEmpty()) {
                    orders.forEachIndexed { index, order ->
                        println("DEBUG: OrderRepository - Order ${index + 1}: ${order.orderNumber} - ${order.customerName ?: "Sin nombre"} - ${order.deliveryAddress}")
                    }
                } else {
                    println("DEBUG: OrderRepository - ⚠️  SUCCESS but EMPTY list - No orders found")
                }
                Result.success(orders)
            } else {
                val errorBody = response.errorBody()?.string() ?: "No error body"
                println("DEBUG: OrderRepository - ❌ FAILURE!")
                println("DEBUG: OrderRepository - Error Body: '$errorBody'")
                println("DEBUG: OrderRepository - This could be: 401 (auth), 403 (forbidden), 404 (not found), 500 (server error)")

                when (response.code()) {
                    401 -> println("DEBUG: OrderRepository - 401 UNAUTHORIZED - Check authentication token")
                    403 -> println("DEBUG: OrderRepository - 403 FORBIDDEN - Check user permissions")
                    404 -> println("DEBUG: OrderRepository - 404 NOT FOUND - Check endpoint URL")
                    500 -> println("DEBUG: OrderRepository - 500 SERVER ERROR - Check server logs")
                }

                // Special handling for 403 - check user role
                if (response.code() == 403) {
                    println("DEBUG: OrderRepository - 🚫 403 FORBIDDEN - POSSIBLE CAUSES:")
                    println("DEBUG: OrderRepository - 1. User doesn't have PROVIDER role")
                    println("DEBUG: OrderRepository - 2. Endpoint requires different permissions")
                    println("DEBUG: OrderRepository - 3. Backend expects different role format")
                    println("DEBUG: OrderRepository - 4. User is authenticated but not authorized")
                    println("DEBUG: OrderRepository - 💡 SOLUTION: Check user role in database/backend")
                }

                Result.failure(Exception("Error al obtener órdenes: $errorBody (${response.code()})"))
            }
        } catch (e: Exception) {
            println("DEBUG: OrderRepository - 💥 EXCEPTION!")
            println("DEBUG: OrderRepository - Exception: ${e.message}")
            println("DEBUG: OrderRepository - Exception Type: ${e::class.simpleName}")
            println("DEBUG: OrderRepository - Stack Trace: ${e.stackTraceToString()}")
            println("DEBUG: OrderRepository - This could be network, SSL, or parsing issue")
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

