package com.r0ggdev.fueltrack.provider.data.dto

data class ProviderAnalyticsDto(
    val totalOrders: Int,
    val pendingOrders: Int,
    val completedOrders: Int,
    val totalRevenue: Double,
    val activeVehicles: Int,
    val availableOperators: Int,
    val monthlyRevenue: List<MonthlyRevenueDto>,
    val fuelTypeStats: List<FuelTypeStatDto>
)

data class MonthlyRevenueDto(
    val month: String, // YYYY-MM
    val revenue: Double
)

data class FuelTypeStatDto(
    val fuelType: String,
    val count: Int,
    val totalAmount: Double
)

