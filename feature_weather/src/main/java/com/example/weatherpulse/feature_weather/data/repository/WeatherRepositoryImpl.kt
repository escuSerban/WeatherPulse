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

    override suspend fun getWeatherForecasts(lat: Double, lon: Double): Pair<WeatherForecast, List<WeatherForecast>> {
        val response = api.getOneCallWeather(lat, lon)
        val todaysWeather = response.current.toWeatherForecast()
        val weeklyWeather = response.daily.map { it.toWeatherForecast() }
        return Pair(todaysWeather, weeklyWeather)
    }

    override suspend fun getCoordinates(city: String): Pair<Double, Double>? {
        val response = api.getCoordinatesForCity(city)
        // Return the coordinates of the first result, or null if the list is empty.
        return response.firstOrNull()?.let { it.lat to it.lon }
    }
}
