package com.example.weatherpulse.feature_weather.data.mappers

import com.example.weatherpulse.feature_weather.data.remote.dto.CurrentWeatherDto
import com.example.weatherpulse.feature_weather.data.remote.dto.DailyWeatherDto
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast

/**
 * Maps a CurrentWeatherDto from the data layer to a WeatherForecast domain model.
 */
fun CurrentWeatherDto.toWeatherForecast(): WeatherForecast {
    return WeatherForecast(
        date = this.dt,
        temp = this.temp,
        description = this.weather.firstOrNull()?.description.orEmpty(),
        icon = this.weather.firstOrNull()?.icon.orEmpty(),
        humidity = this.humidity,
        windSpeed = this.windSpeed
    )
}

/**
 * Maps a DailyWeatherDto from the data layer to a WeatherForecast domain model.
 */
fun DailyWeatherDto.toWeatherForecast(): WeatherForecast {
    return WeatherForecast(
        date = this.dt,
        temp = this.temp.day,
        description = this.weather.firstOrNull()?.description.orEmpty(),
        icon = this.weather.firstOrNull()?.icon.orEmpty(),
        humidity = this.humidity,
        windSpeed = this.windSpeed
    )
}
