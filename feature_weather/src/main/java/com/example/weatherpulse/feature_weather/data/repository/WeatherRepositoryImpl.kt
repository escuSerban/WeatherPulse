package com.example.weatherpulse.feature_weather.data.repository

import com.example.weatherpulse.feature_weather.data.remote.WeatherApi
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getTodaysWeather(city: String): WeatherForecast {
        val coordinates = api.getCoordinates(city, 1).first()
        val weatherData = api.getWeatherData(coordinates.lat, coordinates.lon)
        return WeatherForecast(
            city = coordinates.name,
            temperature = weatherData.current.temp,
            description = weatherData.current.weather.first().description
        )
    }

    override suspend fun getWeeklyWeather(city: String): List<WeatherForecast> {
        val coordinates = api.getCoordinates(city, 1).first()
        val weatherData = api.getWeatherData(coordinates.lat, coordinates.lon)
        return weatherData.daily.map { dailyWeather ->
            WeatherForecast(
                city = coordinates.name,
                temperature = dailyWeather.temp.day,
                description = dailyWeather.weather.first().description
            )
        }
    }
}