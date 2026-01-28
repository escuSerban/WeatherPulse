package com.example.weatherpulse.feature_weather.data.repository

import com.example.weatherpulse.feature_weather.data.mappers.toWeatherForecast
import com.example.weatherpulse.feature_weather.data.remote.WeatherApi
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Implementation of the [WeatherRepository] interface.
 */
class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi) : WeatherRepository {

    // In a real app, you would use a Geocoding API.
    private val cityCoordinates = mapOf(
        "london" to (51.5074 to -0.1278),
        "tokyo" to (35.6895 to 139.6917),
        "new york" to (40.7128 to -74.0060),
        "berlin" to (52.5200 to 13.4050)
    )

    override suspend fun getWeatherForecasts(lat: Double, lon: Double): Pair<WeatherForecast, List<WeatherForecast>> {
        val response = api.getWeatherData(lat, lon)
        val todaysWeather = response.current.toWeatherForecast()
        val weeklyWeather = response.daily.map { it.toWeatherForecast() }
        return Pair(todaysWeather, weeklyWeather)
    }

    override suspend fun getCoordinates(city: String): Pair<Double, Double>? {
        return cityCoordinates[city.lowercase()]
    }
}