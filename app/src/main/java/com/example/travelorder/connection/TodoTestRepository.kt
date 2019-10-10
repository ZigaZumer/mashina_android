package com.example.travelorder.connection

class TodoTestRepository {
    var client: WebserviceTest = RetrofitBuilderTest.webservice

    suspend fun getTodoTest(id: Int) = client.getTodo(id)
}