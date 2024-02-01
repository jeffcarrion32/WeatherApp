package com.example.weatherapp.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFit {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    val api: MyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MyApi::class.java)
}