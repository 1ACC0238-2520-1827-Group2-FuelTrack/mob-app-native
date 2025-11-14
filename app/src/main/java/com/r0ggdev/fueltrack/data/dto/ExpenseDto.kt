package com.r0ggdev.fueltrack.data.dto

data class ExpenseDto(
    val id: String,
    val vehicleId: String,
    val amount: Double,
    val type: String,
    val description: String,
    val date: String
)

data class CreateExpenseRequest(
    val vehicleId: String,
    val amount: Double,
    val type: String,
    val description: String,
    val date: String
)

data class UpdateExpenseRequest(
    val amount: Double,
    val type: String,
    val description: String,
    val date: String
)

