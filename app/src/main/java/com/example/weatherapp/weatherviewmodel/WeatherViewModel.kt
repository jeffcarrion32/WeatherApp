package com.example.weatherapp.weatherviewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.weatherapiresponse.NetworkStateResponse
import com.example.weatherapp.weatherapiresponse.WeatherResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    val repository: WeatherRepository = WeatherRepository(),
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {
    private val _response: MutableState<NetworkStateResponse?> = mutableStateOf(NetworkStateResponse.Loading)
    val response = _response
    var weather: WeatherResponse? = null

    fun makeRequest(value: String) {
        viewModelScope.launch {
            try {
                val response = repository.getWeatherResponse(dispatcher, value)
                _response.value = NetworkStateResponse.Success(response)
                weather = response
            } catch (
                ex:Exception
            ){
                _response.value = NetworkStateResponse.Failure("Network Failed")
            }
        }
    }

}