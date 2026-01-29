package com.example.weatherpulse.feature_weather.data.remote

import com.example.weatherpulse.feature_weather.data.remote.dto.GeocodingDto
import com.example.weatherpulse.feature_weather.data.remote.dto.OneCallDto
import com.example.weatherpulse.feature_weather.data.remote.dto.ReverseGeocodingDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Defines the endpoints for the OpenWeatherMap API.
 */
interface WeatherApi {

    @GET("data/3.0/onecall")
    suspend fun getOneCallWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): OneCallDto

    @GET("geo/1.0/direct")
    suspend fun getCoordinatesForCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1
    ): List<GeocodingDto>

    @GET("geo/1.0/reverse")
    suspend fun getCityForCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1
    ): List<ReverseGeocodingDto>
}
