package com.example.weatherpulse.feature_weather.domain.repository

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast

interface WeatherRepository {
    suspend fun getTodaysWeather(city: String): WeatherForecast
    suspend fun getWeeklyWeather(city: String): List<WeatherForecast>
}