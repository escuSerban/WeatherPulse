package com.example.weatherpulse.feature_weather.domain.repository

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast

interface WeatherRepository {

    suspend fun getWeatherForecastsByCity(city: String): Pair<WeatherForecast, List<WeatherForecast>>

    suspend fun getWeatherForecastsByCoordinates(lat: Double, lon: Double): Pair<WeatherForecast, List<WeatherForecast>>

    suspend fun getCityForCoordinates(lat: Double, lon: Double): String?
}
