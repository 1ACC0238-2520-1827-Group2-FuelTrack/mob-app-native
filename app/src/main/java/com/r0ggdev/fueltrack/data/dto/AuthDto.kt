package com.r0ggdev.fueltrack.data.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: String? = null
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String = "",
    val role: Int = 1
)

data class RegisterResponse(
    val token: String,
    val userId: String? = null
)

