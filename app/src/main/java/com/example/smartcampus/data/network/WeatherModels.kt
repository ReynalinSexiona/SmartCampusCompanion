package com.example.smartcampus.data.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("is_day")
    val isDay: Int
)
