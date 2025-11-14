package com.r0ggdev.fueltrack.provider.data.dto

data class ProviderVehicleDto(
    val id: String,
    val licensePlate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val capacity: Double,
    val status: String,
    val currentLatitude: Double?,
    val currentLongitude: Double?
)

data class UpdateVehicleLocationRequest(
    val latitude: Double,
    val longitude: Double
)

