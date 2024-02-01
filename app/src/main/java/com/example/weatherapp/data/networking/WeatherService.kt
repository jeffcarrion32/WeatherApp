package com.example.weatherapp.data.networking

import com.example.weatherapp.domain.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET(WEATHER_ENDPOINT)
    suspend fun getWeather(
        @Query("q")
        cityName: String,
        @Query("appid")
        apiKey: String = OPEN_WEATHER_API_KEY
    ): WeatherResponse
}