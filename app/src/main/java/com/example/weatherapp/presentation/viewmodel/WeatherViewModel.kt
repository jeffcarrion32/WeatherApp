package com.example.weatherapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.model.NetworkStateResponse
import com.example.weatherapp.domain.model.WeatherResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _networkResponse: MutableState<NetworkStateResponse?> = mutableStateOf(null)
    val networkResponse = _networkResponse

    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse = _weatherResponse

    var temperature: Double = 0.0

    fun makeWeatherRequest(cityName: String) {
        _networkResponse.value = NetworkStateResponse.Loading
        viewModelScope.launch {
            try {
                val response = repository.getWeatherResponse(dispatcher, cityName)
                _networkResponse.value = NetworkStateResponse.Success(response)
                _weatherResponse.value = response
                handleTemperature(response.main.temp)
            } catch (ex: Exception) {
                _networkResponse.value = NetworkStateResponse.Failure("Network Failed")
            }
        }
    }

    private fun handleTemperature(temp: Double) {
        temperature = ((temp - 273.15) * 9/5) + 32
        temperature = (temperature * 100.0).roundToInt() / 100.0
    }
}