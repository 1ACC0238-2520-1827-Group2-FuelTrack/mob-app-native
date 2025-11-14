package com.r0ggdev.fueltrack.provider.data.remote

import com.r0ggdev.fueltrack.provider.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProviderApiService {
    
    // Analytics
    @GET("Analytics/provider")
    suspend fun getProviderAnalytics(): Response<ProviderAnalyticsDto>
    
    // Orders
    @GET("Orders/provider")
    suspend fun getProviderOrders(): Response<List<OrderDto>>
    
    @GET("Orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<OrderDto>
    
    @PATCH("Orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body request: UpdateOrderStatusRequest
    ): Response<Unit>
    
    // Vehicles
    @GET("Vehicles")
    suspend fun getVehicles(): Response<List<ProviderVehicleDto>>
    
    @GET("Vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<ProviderVehicleDto>
    
    @PATCH("Vehicles/{id}/location")
    suspend fun updateVehicleLocation(
        @Path("id") id: String,
        @Body request: UpdateVehicleLocationRequest
    ): Response<Unit>
    
    // Operators
    @GET("Operators")
    suspend fun getOperators(): Response<List<OperatorDto>>
    
    @GET("Operators/{id}")
    suspend fun getOperatorById(@Path("id") id: String): Response<OperatorDto>
    
    @POST("Operators")
    suspend fun createOperator(@Body request: CreateOperatorRequest): Response<OperatorDto>
    
    @PUT("Operators/{id}")
    suspend fun updateOperator(
        @Path("id") id: String,
        @Body request: UpdateOperatorRequest
    ): Response<OperatorDto>
    
    @DELETE("Operators/{id}")
    suspend fun deleteOperator(@Path("id") id: String): Response<Unit>
    
    // Notifications
    @GET("Notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>
    
    @GET("Notifications/{id}")
    suspend fun getNotificationById(@Path("id") id: String): Response<NotificationDto>
    
    @PATCH("Notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: String): Response<Unit>
    
    // Profile
    @GET("Users/profile")
    suspend fun getProfile(): Response<ProviderProfileDto>
    
    @PUT("Users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProviderProfileDto>
}

