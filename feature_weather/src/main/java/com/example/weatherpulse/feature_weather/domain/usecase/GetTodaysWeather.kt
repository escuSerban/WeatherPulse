package com.example.weatherpulse.feature_weather.domain.usecase

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository

class GetTodaysWeather(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): WeatherForecast {
        return repository.getTodaysWeather(city)
    }
}