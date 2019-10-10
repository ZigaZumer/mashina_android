package com.example.travelorder.connection

import com.example.travelorder.application.TravelOrderApplication
import com.example.travelorder.utils.Constants
import com.example.travelorder.utils.PreferencesUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Apifactory {
    val BASE_URL = "http://dev.razvija.se:8888/"

    private val token = PreferencesUtil(TravelOrderApplication.appContext).getString(Constants.TOKEN)

    private val authInterceptor = Interceptor{chain ->
        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${PreferencesUtil(TravelOrderApplication.appContext).getString(Constants.TOKEN)}").build()

            chain.proceed(newRequest)

    }

    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val httpClient = OkHttpClient().newBuilder().addInterceptor(authInterceptor).addInterceptor(loggingInterceptor)

    val travelOrderApi: TravelOrderApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(TravelOrderApi::class.java)
    }
}