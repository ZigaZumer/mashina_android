package com.example.travelorder.connection

import com.example.travelorder.model.TodoTest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WebserviceTest {
    @GET("/todos/{id}")
    suspend fun getTodo(@Path(value = "id") todoId: Int): TodoTest
}