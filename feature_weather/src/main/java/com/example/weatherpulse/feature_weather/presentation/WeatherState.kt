package com.example.weatherpulse.feature_weather.presentation

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast

data class WeatherState(
    val todaysWeather: WeatherForecast? = null,
    val weeklyWeather: List<WeatherForecast> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)
