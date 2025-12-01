package com.r0ggdev.fueltrack.provider.data.dto

data class OperatorDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val licenseNumber: String,
    val licenseExpiryDate: String,
    val isActive: Boolean
)

data class CreateOperatorRequest(
    val firstName: String,
    val lastName: String,
    val licenseNumber: String,
    val licenseExpiryDate: String,
    val phone: String
)

data class UpdateOperatorRequest(
    val firstName: String,
    val lastName: String,
    val licenseNumber: String,
    val licenseExpiryDate: String,
    val phone: String,
    val isActive: Boolean
)

