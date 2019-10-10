package com.example.travelorder.connection

import com.example.travelorder.model.*
import retrofit2.Response
import retrofit2.http.*

interface TravelOrderApi {

    @GET("api/travelexpenses")
    suspend fun getTravelOrderHistory(): Response<List<Order>>

    @GET("api/vehicles")
    suspend fun getVehicles(): Response<List<Vehicle>>

    @GET("api/purposes")
    suspend fun getPurposes(): Response<List<TravelPurpose>>

    @GET("api/employees")
    suspend fun getEmployees(): Response<List<Employee>>

    @POST("api/authenticate")
    suspend fun postRegistration(@Body registrationUser: RegistrationUser) : Response<User>

    @POST("api/expensereport")
    suspend fun postOpenOrder(@Body openOrder: OpenOrder): Response<GeneralResponse>

    @POST("api/travelexpense/finalize/{id}")
    suspend fun postCloseOrder(@Path(value = "id") id: Int, @Body openOrder: OpenOrder): Response<GeneralResponse>

    @PATCH("api/travelexpense/finalize/{id}")
    suspend fun patchCloseOrdrer(@Path(value = "id") id: Int, @Body closeOrder: CloseOrder): Response<GeneralResponse>
}