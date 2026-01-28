package com.example.weatherpulse.feature_weather.domain.repository

import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast

/**
 * Repository interface for fetching weather-related data.
 */
interface WeatherRepository {

    /**
     * Fetches both the current weather and the weekly forecast for a given location.
     *
     * @param lat The latitude.
     * @param lon The longitude.
     * @return A pair containing today's weather and a list of the weekly forecast.
     */
    suspend fun getWeatherForecasts(lat: Double, lon: Double): Pair<WeatherForecast, List<WeatherForecast>>

    /**
     * Fetches the geographical coordinates for a given city name.
     *
     * @param city The name of the city.
     * @return A Pair of (latitude, longitude) or null if the city is not found.
     */
    suspend fun getCoordinates(city: String): Pair<Double, Double>?
}
