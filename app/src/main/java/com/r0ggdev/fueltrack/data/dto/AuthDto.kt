package com.r0ggdev.fueltrack.data.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: String,
    val user: UserDto
)
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
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
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: String,
    val user: UserDto
)
