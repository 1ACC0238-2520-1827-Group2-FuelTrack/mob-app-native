package com.r0ggdev.fueltrack.data.dto

data class VehicleDto(
    val id: String,
    val userId: String,
    val brand: String,
    val model: String,
    val plate: String,
    val year: Int,
    val tankCapacity: Double
)

data class CreateVehicleRequest(
    val licensePlate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val capacity: Int
)

data class UpdateVehicleRequest(
    val brand: String,
    val model: String,
    val plate: String,
    val year: Int,
    val tankCapacity: Double
)

