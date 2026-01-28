package com.example.weatherpulse.feature_weather.domain.model

data class WeatherForecast(
    val date: Long,
    val temp: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val icon: String
)
