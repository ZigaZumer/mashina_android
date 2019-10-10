package com.example.travelorder.history

import com.example.travelorder.connection.Apifactory
import com.example.travelorder.connection.TravelOrderApi
import com.example.travelorder.model.CloseOrder
import com.example.travelorder.model.OpenOrder
import com.example.travelorder.model.RegistrationUser

class OrderRepository {

    var client: TravelOrderApi = Apifactory.travelOrderApi

    suspend fun authenticateUser(registrationUser: RegistrationUser) = client.postRegistration(registrationUser)

    suspend fun getOrdersListAll() = client.getTravelOrderHistory()

    suspend fun getVehicles() = client.getVehicles()

    suspend fun getPurposes() = client.getPurposes()

    suspend fun getEmployees() = client.getEmployees()

    suspend fun openOrder(openOrder: OpenOrder) = client.postOpenOrder(openOrder)

    suspend fun closeOrder(id: Int, openOrder: OpenOrder) = client.postCloseOrder(id, openOrder)

    suspend fun patchClose(id: Int, closeOrder: CloseOrder) = client.patchCloseOrdrer(id, closeOrder)
}