package com.example.weatherapp.data.repository

import com.example.weatherapp.domain.model.WeatherResponse
import com.example.weatherapp.data.networking.WeatherApiClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeatherRepository(val weatherApiClient: WeatherApiClient) {
    suspend fun getWeatherResponse(dispatcher: CoroutineDispatcher, value: String): WeatherResponse {
        return withContext(dispatcher) {
            val response = weatherApiClient.api.getWeather(cityName = value)

            response
        }
    }
}