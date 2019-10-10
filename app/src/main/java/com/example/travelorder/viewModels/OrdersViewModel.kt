package com.example.travelorder.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.travelorder.connection.TodoTestRepository
import com.example.travelorder.history.OrderRepository
import com.example.travelorder.model.*
import kotlinx.coroutines.*

class OrdersViewModel : ViewModel() {

    var selectedOrder: Order? = null

    val user = MutableLiveData<User>()

    val travelOrdersList = MutableLiveData<List<Order>>()

    val vehiclesList = MutableLiveData<List<Vehicle>>()

    val purposesList = MutableLiveData<List<TravelPurpose>>()

    val employeeList = MutableLiveData<List<Employee>>()

    val openOrderResponseObject = MutableLiveData<GeneralResponse>()

    val closedOrderResponseObject = MutableLiveData<GeneralResponse>()

    private var orderRepository: OrderRepository = OrderRepository()
    //    val authorizationUser = liveData(Dispatchers.IO){
//        val user = orderRepository.authenticateUser()
//    }
    fun resetOrderRepository(){
        orderRepository = OrderRepository()
    }

    fun getUser(registrationUser: RegistrationUser){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.authenticateUser(registrationUser)
            response?.let {
                if (response.isSuccessful){
                    user.postValue(response?.body())
                }else{
                    user.postValue(User(null, "", "", "", "", "", response?.message(), response?.code().toString(), "", ""))
                }
            }

        }
    }

    fun getTravelOrdersHistory(){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.getOrdersListAll()
            if (response?.isSuccessful!!){
                travelOrdersList.postValue(response.body()?.sortedByDescending { it.id })
            }
        }
    }

    fun getVehicles(){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.getVehicles()
            if (response.isSuccessful){
                vehiclesList.postValue(response.body())
            }
        }
    }

    fun getPurposes(){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.getPurposes()
            if (response.isSuccessful){
                purposesList.postValue(response.body())
            }
        }
    }

    fun getEmployees(){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.getEmployees()
            if (response.isSuccessful){
                employeeList.postValue(response.body())
            }
        }
    }

    fun startNewTravelOrder(openOrder: OpenOrder){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
            val response = orderRepository.openOrder(openOrder)
            if (response.isSuccessful){
                openOrderResponseObject.postValue(response.body())
            }
        }
    }

    fun finishNewTravelOrder(id: Int, openOrder: OpenOrder){
        val ceh = CoroutineExceptionHandler { _, throwable -> throwable.printStackTrace() }
        CoroutineScope(Dispatchers.IO).launch(ceh) {
//            val response = orderRepository.patchClose(id, closeOrder)
            val response = orderRepository.closeOrder(id, openOrder)
            if (response.isSuccessful){
                closedOrderResponseObject.postValue(response.body())
            }
        }
    }

    val todoRepository: TodoTestRepository = TodoTestRepository()
    val firstTodoTest = liveData(Dispatchers.IO){
        val retrivedTodoTest = todoRepository.getTodoTest(1)
        emit(retrivedTodoTest)
    }
}