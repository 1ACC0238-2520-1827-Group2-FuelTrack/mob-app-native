package com.r0ggdev.fueltrack.provider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.provider.data.dto.ProviderVehicleDto
import com.r0ggdev.fueltrack.provider.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VehicleUiState(
    val vehicles: List<ProviderVehicleDto> = emptyList(),
    val selectedVehicle: ProviderVehicleDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VehicleViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(VehicleUiState())
    val uiState: StateFlow<VehicleUiState> = _uiState.asStateFlow()
    
    fun loadVehicles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            vehicleRepository.getVehicles()
                .onSuccess { vehicles ->
                    _uiState.value = _uiState.value.copy(
                        vehicles = vehicles,
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
    
    fun loadVehicleById(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            vehicleRepository.getVehicleById(id)
                .onSuccess { vehicle ->
                    _uiState.value = _uiState.value.copy(
                        selectedVehicle = vehicle,
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
    
    fun updateVehicleLocation(id: String, latitude: Double, longitude: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            vehicleRepository.updateVehicleLocation(id, latitude, longitude)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    loadVehicleById(id)
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

