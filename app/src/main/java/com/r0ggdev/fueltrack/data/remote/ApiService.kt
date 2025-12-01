package com.r0ggdev.fueltrack.data.remote

import com.r0ggdev.fueltrack.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // Vehicles
    @GET("api/Vehicles")
    suspend fun getVehicles(): Response<List<VehicleDto>>

    @GET("api/Vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<VehicleDto>

    @POST("api/Vehicles")
    suspend fun createVehicle(@Body request: CreateVehicleRequest): Response<VehicleDto>

    @PUT("api/Vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body request: UpdateVehicleRequest): Response<VehicleDto>

    @DELETE("api/Vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") id: String): Response<Unit>

    // Fuel Records
    @GET("api/FuelRecords/vehicle/{vehicleId}")
    suspend fun getFuelRecordsByVehicle(@Path("vehicleId") vehicleId: String): Response<List<FuelRecordDto>>

    @GET("api/FuelRecords/{vehicleId}/latest")
    suspend fun getLatestFuelRecord(@Path("vehicleId") vehicleId: String): Response<FuelRecordDto>

    @POST("api/FuelRecords")
    suspend fun createFuelRecord(@Body request: CreateFuelRecordRequest): Response<FuelRecordDto>

    @PUT("api/FuelRecords/{id}")
    suspend fun updateFuelRecord(@Path("id") id: String, @Body request: UpdateFuelRecordRequest): Response<FuelRecordDto>

    @DELETE("api/FuelRecords/{id}")
    suspend fun deleteFuelRecord(@Path("id") id: String): Response<Unit>

    // Expenses
    @GET("api/Expenses/vehicle/{vehicleId}")
    suspend fun getExpensesByVehicle(@Path("vehicleId") vehicleId: String): Response<List<ExpenseDto>>

    @POST("api/Expenses")
    suspend fun createExpense(@Body request: CreateExpenseRequest): Response<ExpenseDto>

    @PUT("api/Expenses/{id}")
    suspend fun updateExpense(@Path("id") id: String, @Body request: UpdateExpenseRequest): Response<ExpenseDto>

    @DELETE("api/Expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: String): Response<Unit>

    // Dashboard
    @GET("api/Analytics/client/{vehicleId}")
    suspend fun getDashboard(@Path("vehicleId") vehicleId: String): Response<DashboardDto>
}


