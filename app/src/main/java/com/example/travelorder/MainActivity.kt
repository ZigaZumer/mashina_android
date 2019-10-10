package com.example.travelorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.travelorder.viewModels.OrdersViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var ordersViewModel: OrdersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewModel()
    }

    private fun initViewModel(){
        ordersViewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)
    }
}
