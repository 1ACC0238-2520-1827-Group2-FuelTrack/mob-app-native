package com.r0ggdev.fueltrack.provider.data.dto

data class NotificationDto(
    val id: String,
    val title: String,
    val message: String,
    val relatedOrderNumber: String?,
    val createdAt: String,
    val isRead: Boolean
)

