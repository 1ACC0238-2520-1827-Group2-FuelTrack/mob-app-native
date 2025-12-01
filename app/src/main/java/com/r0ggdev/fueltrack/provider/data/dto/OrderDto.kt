package com.r0ggdev.fueltrack.provider.data.dto

data class OrderDto(
    val id: String,
    val orderNumber: String,
    val fuelType: String,
    val quantity: Double,
    val pricePerLiter: Double,
    val totalAmount: Double,
    val status: Int,
    val deliveryAddress: String,
    val deliveryLatitude: Double?,
    val deliveryLongitude: Double?,
    val estimatedDeliveryTime: String?,
    val actualDeliveryTime: String?,
    val createdAt: String,
    val customerName: String?,
    val assignedVehiclePlate: String?,
    val assignedOperatorName: String?
)

data class UpdateOrderStatusRequest(
    val status: Int
)

