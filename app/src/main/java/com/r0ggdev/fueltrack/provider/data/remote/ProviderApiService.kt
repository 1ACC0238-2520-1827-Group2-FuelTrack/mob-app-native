package com.r0ggdev.fueltrack.provider.data.remote

import com.r0ggdev.fueltrack.provider.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ProviderApiService {
    
    // Analytics
    @GET("api/Analytics/provider")
    suspend fun getProviderAnalytics(): Response<ProviderAnalyticsDto>

    // Orders
    @GET("api/Orders/provider")
    suspend fun getProviderOrders(): Response<List<OrderDto>>

    @GET("api/Orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Response<OrderDto>

    @PATCH("api/Orders/{id}/status")
    suspend fun updateOrderStatus(
        @Path("id") id: String,
        @Body request: UpdateOrderStatusRequest
    ): Response<Unit>
    
    // Vehicles
    @GET("api/Vehicles")
    suspend fun getVehicles(): Response<List<ProviderVehicleDto>>

    @GET("api/Vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<ProviderVehicleDto>

    @POST("api/Vehicles")
    suspend fun createVehicle(@Body request: CreateVehicleRequest): Response<ProviderVehicleDto>

    @PATCH("api/Vehicles/{id}/location")
    suspend fun updateVehicleLocation(
        @Path("id") id: String,
        @Body request: UpdateVehicleLocationRequest
    ): Response<Unit>
    
    // Operators
    @GET("api/Operators")
    suspend fun getOperators(): Response<List<OperatorDto>>

    @GET("api/Operators/{id}")
    suspend fun getOperatorById(@Path("id") id: String): Response<OperatorDto>

    @POST("api/Operators")
    suspend fun createOperator(@Body request: CreateOperatorRequest): Response<OperatorDto>

    @PUT("api/Operators/{id}")
    suspend fun updateOperator(
        @Path("id") id: String,
        @Body request: UpdateOperatorRequest
    ): Response<OperatorDto>

    @DELETE("api/Operators/{id}")
    suspend fun deleteOperator(@Path("id") id: String): Response<Unit>

    // Notifications
    @GET("api/Notifications")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @GET("api/Notifications/{id}")
    suspend fun getNotificationById(@Path("id") id: String): Response<NotificationDto>

    @PATCH("api/Notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: String): Response<Unit>

    // Profile
    @GET("api/Users/profile")
    suspend fun getProfile(): Response<ProviderProfileDto>

    @PUT("api/Users/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<ProviderProfileDto>
}

