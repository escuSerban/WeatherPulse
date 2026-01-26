package com.example.weatherpulse.feature_weather.domain.usecase

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository

class GetWeeklyWeather(private val repository: WeatherRepository) {
    suspend operator fun invoke(city: String): List<WeatherForecast> {
        return repository.getWeeklyWeather(city)
    }
}