package com.example.weatherpulse.feature_weather.domain.usecase

import com.example.weatherpulse.core.utils.Resource
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * A use case that orchestrates fetching weather data for a given city.
 */
class GetWeather @Inject constructor(
    private val repository: WeatherRepository
) {

    operator fun invoke(city: String): Flow<Resource<Pair<WeatherForecast, List<WeatherForecast>>>> = flow {
        emit(Resource.Loading())
        try {
            val coordinates = repository.getCoordinates(city)
            if (coordinates != null) {
                val (lat, lon) = coordinates
                val weatherData = repository.getWeatherForecasts(lat, lon)
                emit(Resource.Success(weatherData))
            } else {
                emit(Resource.Error("City not found or invalid."))
            }
        } catch (e: Exception) {
            // Exceptions from the repository layer are caught here and emitted as an Error resource.
            emit(Resource.Error(e.message ?: "An unknown error occurred while fetching weather data."))
        }
    }
}
