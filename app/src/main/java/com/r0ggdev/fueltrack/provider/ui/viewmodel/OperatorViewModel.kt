package com.r0ggdev.fueltrack.provider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.provider.data.dto.CreateOperatorRequest
import com.r0ggdev.fueltrack.provider.data.dto.OperatorDto
import com.r0ggdev.fueltrack.provider.data.dto.UpdateOperatorRequest
import com.r0ggdev.fueltrack.provider.data.repository.OperatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OperatorUiState(
    val operators: List<OperatorDto> = emptyList(),
    val selectedOperator: OperatorDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OperatorViewModel @Inject constructor(
    private val operatorRepository: OperatorRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OperatorUiState())
    val uiState: StateFlow<OperatorUiState> = _uiState.asStateFlow()
    
    fun loadOperators() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            operatorRepository.getOperators()
                .onSuccess { operators ->
                    _uiState.value = _uiState.value.copy(
                        operators = operators,
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
    
    fun loadOperatorById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            operatorRepository.getOperatorById(id)
                .onSuccess { operator ->
                    _uiState.value = _uiState.value.copy(
                        selectedOperator = operator,
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
    
    fun createOperator(request: CreateOperatorRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            operatorRepository.createOperator(request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadOperators()
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
    
    fun updateOperator(id: String, request: UpdateOperatorRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            operatorRepository.updateOperator(id, request)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadOperators()
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
    
    fun deleteOperator(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            operatorRepository.deleteOperator(id)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadOperators()
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

