package com.example.weatherpulse.feature_weather.data.remote.dto

/**
 * Data Transfer Object for the response from the OpenWeatherMap Geocoding API.
 * It represents a single location result.
 */
data class GeocodingDto(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)
