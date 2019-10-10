package com.example.travelorder.application

import android.app.Application
import android.content.Context

class TravelOrderApplication : Application(){
    companion object{
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}