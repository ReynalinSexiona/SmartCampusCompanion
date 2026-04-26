package com.example.smartcampus.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcampus.data.network.WeatherApiService
import com.example.smartcampus.data.network.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherResponse?>(null)
    val weatherState: StateFlow<WeatherResponse?> = _weatherState

    private val _isWeatherLoading = MutableStateFlow(false)
    val isWeatherLoading: StateFlow<Boolean> = _isWeatherLoading

    private val apiService = WeatherApiService.create()

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _isWeatherLoading.value = true
            try {
                // Manila Coordinates
                val response = apiService.getCurrentWeather(14.5995, 120.9842)
                _weatherState.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isWeatherLoading.value = false
            }
        }
    }
}
