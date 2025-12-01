package com.r0ggdev.fueltrack.provider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.data.local.PreferencesManager
import com.r0ggdev.fueltrack.provider.data.dto.OrderDto
import com.r0ggdev.fueltrack.provider.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderUiState(
    val orders: List<OrderDto> = emptyList(),
    val selectedOrder: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasRealData: Boolean = false
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            println("DEBUG: OrderViewModel - ==========================================")
            println("DEBUG: OrderViewModel - LOADING ORDERS FROM BACKEND")
            println("DEBUG: OrderViewModel - This endpoint requires PROVIDER role")
            println("DEBUG: OrderViewModel - If you get 403 Forbidden, check:")
            println("DEBUG: OrderViewModel -   1. User role stored in app")
            println("DEBUG: OrderViewModel -   2. Backend expects 'PROVIDER' role")
            println("DEBUG: OrderViewModel -   3. User has correct permissions")
            println("DEBUG: OrderViewModel - ==========================================")

            // Check stored role directly from preferences
            println("DEBUG: OrderViewModel - 🔍 CHECKING STORED ROLE IN PREFERENCES...")
            try {
                val storedRole = preferencesManager.userRole.firstOrNull()
                println("DEBUG: OrderViewModel - Stored role in preferences: '$storedRole'")
                if (storedRole?.uppercase() != "PROVIDER") {
                    println("DEBUG: OrderViewModel - 🚫 STORED ROLE IS NOT 'PROVIDER' - THIS CAUSES 403!")
                    println("DEBUG: OrderViewModel - 💡 SOLUTION: User needs PROVIDER role in backend")
                } else {
                    println("DEBUG: OrderViewModel - ✅ STORED ROLE IS 'PROVIDER' - Should work")
                }
            } catch (e: Exception) {
                println("DEBUG: OrderViewModel - Error checking stored role: ${e.message}")
            }
            println("DEBUG: OrderViewModel - ==========================================")

            println("DEBUG: OrderViewModel - Calling orderRepository.getProviderOrders()")

            // Llamar a la API real
            orderRepository.getProviderOrders()
                .onSuccess { realOrders ->
                    println("DEBUG: OrderViewModel - SUCCESS! Real orders loaded: ${realOrders.size}")
                    realOrders.forEachIndexed { index, order ->
                        println("DEBUG: Real Order #${index + 1}:")
                        println("  - ID: ${order.id}")
                        println("  - Order Number: ${order.orderNumber}")
                        println("  - Customer: ${order.customerName ?: "Sin nombre"}")
                        println("  - Address: ${order.deliveryAddress}")
                        println("  - Fuel: ${order.fuelType} - ${order.quantity}L")
                        println("  - Amount: ${order.totalAmount}")
                        println("  - Status: ${order.status}")
                        println("  - Created: ${order.createdAt}")
                        println("  ---")
                    }

                    _uiState.value = _uiState.value.copy(
                        orders = realOrders.sortedByDescending { it.createdAt },
                        isLoading = false,
                        hasRealData = true,
                        error = null
                    )
                }
                .onFailure { exception ->
                    println("DEBUG: OrderViewModel - API FAILURE: ${exception.message}")
                    println("DEBUG: OrderViewModel - Exception type: ${exception::class.simpleName}")

                    val errorMessage = when {
                        exception.message?.contains("401") == true ||
                        exception.message?.contains("Unauthorized") == true -> {
                            "Usuario no autenticado. Inicia sesión para ver tus órdenes."
                        }
                        exception.message?.contains("404") == true -> {
                            "Endpoint no encontrado. Verifica la configuración del servidor."
                        }
                        exception.message?.contains("500") == true -> {
                            "Error interno del servidor. Inténtalo más tarde."
                        }
                        else -> {
                            "Error al cargar órdenes: ${exception.message}"
                        }
                    }

                    println("DEBUG: OrderViewModel - Error message for UI: $errorMessage")

                    // Mostrar lista vacía en caso de error
                    _uiState.value = _uiState.value.copy(
                        orders = emptyList(),
                        isLoading = false,
                        hasRealData = false,
                        error = errorMessage
                    )
                }
        }
    }
    
    fun loadOrderById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            println("DEBUG: OrderViewModel - Loading order by ID: $id")

            orderRepository.getOrderById(id)
                .onSuccess { order ->
                    println("DEBUG: OrderViewModel - Order loaded successfully: ${order.orderNumber}")
                    _uiState.value = _uiState.value.copy(
                        selectedOrder = order,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    println("DEBUG: OrderViewModel - Failed to load order: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    fun updateOrderStatus(id: String, status: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            orderRepository.updateOrderStatus(id, status)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadOrders()
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

