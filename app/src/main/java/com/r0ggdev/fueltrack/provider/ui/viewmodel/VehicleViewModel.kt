package com.r0ggdev.fueltrack.provider.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r0ggdev.fueltrack.provider.data.dto.CreateVehicleRequest
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
    val error: String? = null,
    val hasRealData: Boolean = false
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

            println("DEBUG: VehicleViewModel - Loading vehicles from API...")

            vehicleRepository.getVehicles()
                .onSuccess { realVehicles ->
                    println("DEBUG: VehicleViewModel - Success! Real vehicles loaded: ${realVehicles.size}")
                    realVehicles.forEachIndexed { index, vehicle ->
                        println("DEBUG: Real Vehicle #${index + 1}:")
                        println("  - ID: ${vehicle.id}")
                        println("  - Placa: ${vehicle.licensePlate}")
                        println("  - Marca/Modelo: ${vehicle.brand} ${vehicle.model}")
                        println("  - Año: ${vehicle.year}")
                        println("  - Capacidad: ${vehicle.capacity}L")
                        println("  - Estado: ${vehicle.status}")
                        println("  - Ubicación: ${vehicle.currentLatitude}, ${vehicle.currentLongitude}")
                        println("  ---")
                    }

                    _uiState.value = _uiState.value.copy(
                        vehicles = realVehicles,
                        isLoading = false,
                        hasRealData = true,
                        error = null
                    )
                }
                .onFailure { exception ->
                    println("DEBUG: VehicleViewModel - API Error: ${exception.message}")
                    println("DEBUG: VehicleViewModel - No vehicles available")

                    // Si la API falla, mostrar lista vacía
                    _uiState.value = _uiState.value.copy(
                        vehicles = emptyList(),
                        isLoading = false,
                        hasRealData = false,
                        error = "Error al cargar vehículos: ${exception.message}"
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

    fun createVehicle(request: CreateVehicleRequest, onSuccess: () -> Unit) {
        println("DEBUG: VehicleViewModel - ==========================================")
        println("DEBUG: VehicleViewModel - CREATE VEHICLE ATTEMPT")
        println("DEBUG: VehicleViewModel - Vehicle: ${request.licensePlate} - ${request.brand} ${request.model}")
        println("DEBUG: VehicleViewModel - Year: ${request.year}, Capacity: ${request.capacity}")
        println("DEBUG: VehicleViewModel - IMPORTANT: Check user has PROVIDER role permissions!")
        println("DEBUG: VehicleViewModel - ==========================================")

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            println("DEBUG: VehicleViewModel - Calling vehicleRepository.createVehicle")
            println("DEBUG: VehicleViewModel - Calling vehicleRepository.createVehicle")
            vehicleRepository.createVehicle(request)
                .onSuccess { createdVehicle ->
                    println("DEBUG: VehicleViewModel - ✅ Vehicle created successfully: ${createdVehicle.licensePlate}")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    println("DEBUG: VehicleViewModel - Reloading vehicles list...")
                    loadVehicles()
                    println("DEBUG: VehicleViewModel - Calling onSuccess callback")
                    onSuccess()
                }
                .onFailure { exception ->
                    println("DEBUG: VehicleViewModel - ❌ Vehicle creation failed: ${exception.message}")

                    // Provide specific guidance for 403 errors
                    val errorMessage = if (exception.message?.contains("403") == true) {
                        "Error 403: No tienes permisos para crear vehículos. Configura el rol 'Proveedor' en el backend para permitir creación de vehículos."
                    } else {
                        exception.message ?: "Error desconocido"
                    }

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

