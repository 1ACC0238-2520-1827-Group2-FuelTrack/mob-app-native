package com.r0ggdev.fueltrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.data.dto.CreateFuelRecordRequest
import com.r0ggdev.fueltrack.data.dto.FuelRecordDto
import com.r0ggdev.fueltrack.data.dto.UpdateFuelRecordRequest
import com.r0ggdev.fueltrack.data.repository.FuelRecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FuelRecordUiState(
    val fuelRecords: List<FuelRecordDto> = emptyList(),
    val latestRecord: FuelRecordDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FuelRecordViewModel @Inject constructor(
    private val fuelRecordRepository: FuelRecordRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FuelRecordUiState())
    val uiState: StateFlow<FuelRecordUiState> = _uiState.asStateFlow()
    
    fun loadFuelRecords(vehicleId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            fuelRecordRepository.getFuelRecordsByVehicle(vehicleId)
                .onSuccess { records ->
                    _uiState.value = _uiState.value.copy(
                        fuelRecords = records,
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
    
    fun loadLatestFuelRecord(vehicleId: String) {
        viewModelScope.launch {
            fuelRecordRepository.getLatestFuelRecord(vehicleId)
                .onSuccess { record ->
                    _uiState.value = _uiState.value.copy(latestRecord = record)
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }
    
    fun createFuelRecord(request: CreateFuelRecordRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            fuelRecordRepository.createFuelRecord(request)
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
    
    fun updateFuelRecord(id: String, request: UpdateFuelRecordRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            fuelRecordRepository.updateFuelRecord(id, request)
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
    
    fun deleteFuelRecord(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            fuelRecordRepository.deleteFuelRecord(id)
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

