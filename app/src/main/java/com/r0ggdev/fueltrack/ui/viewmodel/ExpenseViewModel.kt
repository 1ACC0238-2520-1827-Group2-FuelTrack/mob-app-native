package com.r0ggdev.fueltrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.data.dto.CreateExpenseRequest
import com.r0ggdev.fueltrack.data.dto.ExpenseDto
import com.r0ggdev.fueltrack.data.dto.UpdateExpenseRequest
import com.r0ggdev.fueltrack.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseUiState(
    val expenses: List<ExpenseDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()
    
    fun loadExpenses(vehicleId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            expenseRepository.getExpensesByVehicle(vehicleId)
                .onSuccess { expenses ->
                    _uiState.value = _uiState.value.copy(
                        expenses = expenses,
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
    
    fun createExpense(request: CreateExpenseRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            expenseRepository.createExpense(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
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
    
    fun updateExpense(id: String, request: UpdateExpenseRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            expenseRepository.updateExpense(id, request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
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
    
    fun deleteExpense(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            expenseRepository.deleteExpense(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
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

