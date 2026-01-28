package com.example.weatherpulse.feature_weather.domain.model

/**
 * A clean data class representing the weather forecast for a specific day.
 * This is independent of the data source.
 */
data class WeatherForecast(
    val date: Long,
    val temp: Double,
    val description: String,
    val icon: String,
    val sunrise: Long,
    val sunset: Long,
    val humidity: Int,
    val windSpeed: Double
)