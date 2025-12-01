package com.r0ggdev.fueltrack.provider.data.dto

data class ProviderProfileDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?
)

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String?
)

