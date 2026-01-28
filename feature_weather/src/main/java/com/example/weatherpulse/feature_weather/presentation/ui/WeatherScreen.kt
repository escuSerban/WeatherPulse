package com.example.weatherpulse.feature_weather.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.presentation.WeatherViewModel
import com.example.weatherpulse.feature_weather.presentation.util.toFormattedDate

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Bar
        SearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = { viewModel.fetchWeatherForCity(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator()
        } else if (state.error != null) {
            Text(
                text = state.error ?: "An unknown error occurred.",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            // Today's Weather
            state.todaysWeather?.let {
                TodayWeatherSection(it)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Weekly Forecast
            if (state.weeklyWeather.isNotEmpty()) {
                Text(
                    text = "Weekly Forecast",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                WeeklyForecastList(state.weeklyWeather)
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Enter city name...") },
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onSearch(query) }) {
            Text("Search")
        }
    }
}

@Composable
fun TodayWeatherSection(weather: WeatherForecast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Today", style = MaterialTheme.typography.headlineSmall)
            Text(text = "${weather.temp.toInt()}°C", style = MaterialTheme.typography.displayMedium)
            Text(text = weather.description, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherDetailItem("Humidity", "${weather.humidity}%")
                WeatherDetailItem("Wind", "${weather.windSpeed} m/s")
            }
        }
    }
}

@Composable
fun WeeklyForecastList(forecast: List<WeatherForecast>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(forecast) { item ->
            WeeklyWeatherItem(item)
        }
    }
}

@Composable
fun WeeklyWeatherItem(weather: WeatherForecast) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weather.date.toFormattedDate(),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${weather.temp.toInt()}°C",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = weather.description,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
