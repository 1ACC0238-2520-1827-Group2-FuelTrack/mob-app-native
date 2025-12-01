package com.r0ggdev.fueltrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.data.dto.DashboardDto
import com.r0ggdev.fueltrack.data.repository.DashboardRepository
import com.r0ggdev.fueltrack.data.repository.FuelRecordRepository
import com.r0ggdev.fueltrack.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val dashboard: DashboardDto? = null,
    val vehicles: List<com.r0ggdev.fueltrack.data.dto.VehicleDto> = emptyList(),
    val selectedVehicleId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val vehicleRepository: VehicleRepository,
    private val fuelRecordRepository: FuelRecordRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    fun loadDashboard(vehicleId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            dashboardRepository.getDashboard(vehicleId)
                .onSuccess { dashboard ->
                    _uiState.value = _uiState.value.copy(
                        dashboard = dashboard,
                        selectedVehicleId = vehicleId,
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

    fun loadVehicles() {
        viewModelScope.launch {
            vehicleRepository.getVehicles()
                .onSuccess { vehicles ->
                    _uiState.value = _uiState.value.copy(vehicles = vehicles)

                    if (vehicles.isNotEmpty() && _uiState.value.selectedVehicleId == null) {
                        loadDashboard(vehicles.first().id)
                    }
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }


    fun selectVehicle(vehicleId: String) {
        _uiState.value = _uiState.value.copy(selectedVehicleId = vehicleId)
        loadDashboard(vehicleId)
    }
    
    fun refresh() {
        _uiState.value.selectedVehicleId?.let { vehicleId ->
            loadDashboard(vehicleId)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

