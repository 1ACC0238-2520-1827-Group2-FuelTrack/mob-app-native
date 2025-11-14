package com.r0ggdev.fueltrack.data.remote

import com.r0ggdev.fueltrack.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Auth
    @POST("Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
    
    // Vehicles
    @GET("vehicles/{userId}")
    suspend fun getVehicles(@Path("userId") userId: String): Response<List<VehicleDto>>
    
    @POST("vehicles")
    suspend fun createVehicle(@Body request: CreateVehicleRequest): Response<VehicleDto>
    
    @PUT("vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body request: UpdateVehicleRequest): Response<VehicleDto>
    
    @DELETE("vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") id: String): Response<Unit>
    
    // Fuel Records
    @GET("fuel-records/vehicle/{vehicleId}")
    suspend fun getFuelRecordsByVehicle(@Path("vehicleId") vehicleId: String): Response<List<FuelRecordDto>>
    
    @GET("fuel-records/{vehicleId}/latest")
    suspend fun getLatestFuelRecord(@Path("vehicleId") vehicleId: String): Response<FuelRecordDto>
    
    @POST("fuel-records")
    suspend fun createFuelRecord(@Body request: CreateFuelRecordRequest): Response<FuelRecordDto>
    
    @PUT("fuel-records/{id}")
    suspend fun updateFuelRecord(@Path("id") id: String, @Body request: UpdateFuelRecordRequest): Response<FuelRecordDto>
    
    @DELETE("fuel-records/{id}")
    suspend fun deleteFuelRecord(@Path("id") id: String): Response<Unit>
    
    // Expenses
    @GET("expenses/vehicle/{vehicleId}")
    suspend fun getExpensesByVehicle(@Path("vehicleId") vehicleId: String): Response<List<ExpenseDto>>
    
    @POST("expenses")
    suspend fun createExpense(@Body request: CreateExpenseRequest): Response<ExpenseDto>
    
    @PUT("expenses/{id}")
    suspend fun updateExpense(@Path("id") id: String, @Body request: UpdateExpenseRequest): Response<ExpenseDto>
    
    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>
    
    // Dashboard
    @GET("dashboard/{vehicleId}")
    suspend fun getDashboard(@Path("vehicleId") vehicleId: String): Response<DashboardDto>
}

