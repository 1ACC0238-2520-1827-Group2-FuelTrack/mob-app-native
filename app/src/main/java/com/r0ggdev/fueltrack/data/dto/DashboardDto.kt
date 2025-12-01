package com.r0ggdev.fueltrack.data.dto

data class DashboardDto(
    val vehicleId: String,
    val averageConsumption: Double?,
    val monthlyCost: Double?,
    val lastFuelRecord: FuelRecordDto?,
    val recentExpenses: List<ExpenseDto>?,
    val historicalPerformance: List<PerformanceData>?
)

data class PerformanceData(
    val date: String,
    val consumption: Double,
    val cost: Double
)

