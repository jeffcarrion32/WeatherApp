package com.example.weatherapp.repository

import com.example.weatherapp.weatherapiresponse.WeatherResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeatherRepository {
    suspend fun getWeatherResponse(dispatcher: CoroutineDispatcher, value: String): WeatherResponse  {
        return withContext(dispatcher) {
            val response = RetroFitBuilder().api.getWeather(cityName = value)

            response
        }
    }
}