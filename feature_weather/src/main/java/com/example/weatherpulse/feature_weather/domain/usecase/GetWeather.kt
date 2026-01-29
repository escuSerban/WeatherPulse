package com.example.weatherpulse.feature_weather.domain.usecase

import com.example.weatherpulse.core.utils.Resource
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetWeather @Inject constructor(
    private val repository: WeatherRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun byCity(city: String): Flow<Resource<Pair<WeatherForecast, List<WeatherForecast>>>> = flow {
        emit(Resource.Loading())
        try {
            val result = repository.getWeatherForecastsByCity(city)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred."))
        }
    }.flowOn(ioDispatcher)

    fun byCoordinates(lat: Double, lon: Double): Flow<Resource<Pair<WeatherForecast, List<WeatherForecast>>>> = flow {
        emit(Resource.Loading())
        try {
            val result = repository.getWeatherForecastsByCoordinates(lat, lon)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unknown error occurred."))
        }
    }.flowOn(ioDispatcher)
}
