package com.example.weatherapp.weatherapiresponse

sealed class NetworkStateResponse {
    data class Success(val weatherResponse: WeatherResponse): NetworkStateResponse()

    data class Failure(val error: String): NetworkStateResponse()

    data object Loading: NetworkStateResponse()
}