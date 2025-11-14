package com.r0ggdev.fueltrack.data.dto

import java.util.Date

data class FuelRecordDto(
    val id: String,
    val vehicleId: String,
    val odometer: Int,
    val liters: Double,
    val pricePerLiter: Double,
    val fullTank: Boolean,
    val gasStation: String?,
    val notes: String?,
    val date: String
)

data class CreateFuelRecordRequest(
    val vehicleId: String,
    val odometer: Int,
    val liters: Double,
    val pricePerLiter: Double,
    val fullTank: Boolean,
    val gasStation: String?,
    val notes: String?
)

data class UpdateFuelRecordRequest(
    val odometer: Int,
    val liters: Double,
    val pricePerLiter: Double,
    val fullTank: Boolean,
    val gasStation: String?,
    val notes: String?
)

