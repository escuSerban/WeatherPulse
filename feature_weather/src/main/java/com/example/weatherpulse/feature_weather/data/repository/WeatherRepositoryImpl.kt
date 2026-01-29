package com.example.weatherpulse.feature_weather.data.repository

import com.example.weatherpulse.feature_weather.data.mappers.toWeatherForecast
import com.example.weatherpulse.feature_weather.data.remote.WeatherApi
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {

    override suspend fun getWeatherForecastsByCity(city: String): Pair<WeatherForecast, List<WeatherForecast>> {
        val coordinates = api.getCoordinatesForCity(city).firstOrNull()
            ?: throw Exception("City not found")
        return getWeatherForecastsByCoordinates(coordinates.lat, coordinates.lon)
    }

    override suspend fun getWeatherForecastsByCoordinates(lat: Double, lon: Double): Pair<WeatherForecast, List<WeatherForecast>> {
        val remoteForecasts = api.getOneCallWeather(lat, lon)
        val today = remoteForecasts.current.toWeatherForecast()
        val weekly = remoteForecasts.daily.map { it.toWeatherForecast() }
        return today to weekly
    }

    override suspend fun getCityForCoordinates(lat: Double, lon: Double): String? {
        return api.getCityForCoordinates(lat, lon).firstOrNull()?.name
    }
}
