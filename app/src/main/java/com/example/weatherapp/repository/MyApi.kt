package com.example.weatherapp.repository

import com.example.weatherapp.weatherapiresponse.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface MyApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q")
        cityName: String,
        @Query("appid")
        apiKey: String = "d4618bf528e68c9317725f0e38114ce5"
    ): WeatherResponse
}