package com.r0ggdev.fueltrack.provider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.provider.data.dto.OrderDto
import com.r0ggdev.fueltrack.provider.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderUiState(
    val orders: List<OrderDto> = emptyList(),
    val selectedOrder: OrderDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()
    
    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            orderRepository.getProviderOrders()
                .onSuccess { orders ->
                    _uiState.value = _uiState.value.copy(
                        orders = orders.sortedByDescending { it.createdAt },
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    fun loadOrderById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            orderRepository.getOrderById(id)
                .onSuccess { order ->
                    _uiState.value = _uiState.value.copy(
                        selectedOrder = order,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
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

