package com.example.weatherpulse.feature_weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpulse.core.utils.Resource
import com.example.weatherpulse.feature_weather.domain.usecase.GetWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeather: GetWeather,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    /**
     * Fetches weather data for a given city.
     */
    fun fetchWeatherForCity(city: String) {
        viewModelScope.launch(defaultDispatcher) {
            getWeather(city).collect { result ->
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
        }
    }

    /**
     * Updates the search query in the UI state.
     */
    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }
}
