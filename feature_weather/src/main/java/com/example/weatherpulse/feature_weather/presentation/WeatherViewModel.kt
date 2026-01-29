package com.example.weatherpulse.feature_weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpulse.core.location.LocationTracker
import com.example.weatherpulse.core.utils.Resource
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import com.example.weatherpulse.feature_weather.domain.usecase.GetWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetWeather,
    private val locationTracker: LocationTracker,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun fetchWeatherForCity(city: String) {
        getWeather.byCity(city).onEach { result ->
            updateStateFromResult(result, city)
        }.launchIn(viewModelScope)
    }

    fun fetchWeatherForCurrentLocation() {
        viewModelScope.launch {
            // Set loading state immediately
            _state.update { it.copy(isLoading = true, error = null) }

            locationTracker.getCurrentLocation()?.let { location ->
                val cityName =
                    repository.getCityForCoordinates(location.latitude, location.longitude)
                val displayName = cityName ?: "Current Location"

                getWeather.byCoordinates(location.latitude, location.longitude).onEach { result ->
                    updateStateFromResult(result, displayName)
                }.launchIn(viewModelScope)
            } ?: run {
                setPermissionsNotGrantedState()
            }
        }
    }

    fun setPermissionsNotGrantedState() {
        _state.update {
            it.copy(
                isLoading = false,
                error = "Couldn't retrieve forecast for your current location. Make sure to grant permission. Alternatively you can search for a different city."
            )
        }
    }

    private fun updateStateFromResult(
        result: Resource<Pair<WeatherForecast, List<WeatherForecast>>>,
        city: String
    ) {
        when (result) {
            is Resource.Loading -> {
                _state.update { it.copy(isLoading = true, error = null) }
            }

            is Resource.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        todaysWeather = result.data?.first,
                        weeklyWeather = result.data?.second ?: emptyList(),
                        isLoading = false,
                        searchQuery = city,
                        error = null
                    )
                }
            }

            is Resource.Error -> {
                _state.update { it.copy(error = result.message, isLoading = false) }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
