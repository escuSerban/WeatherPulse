package com.example.weatherpulse.feature_weather.data.remote

import com.example.weatherpulse.feature_weather.data.remote.dto.GeoDirectDto
import com.example.weatherpulse.feature_weather.data.remote.dto.OneCallDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/3.0/onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric"
    ): OneCallDto

    @GET("geo/1.0/direct")
    suspend fun getCoordinates(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1
    ): List<GeoDirectDto>

}