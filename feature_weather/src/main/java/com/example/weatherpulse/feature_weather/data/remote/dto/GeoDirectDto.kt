package com.example.weatherpulse.feature_weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeoDirectDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("country")
    val country: String
)