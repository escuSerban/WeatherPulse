package com.example.weatherpulse.feature_weather.presentation.ui

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weatherpulse.feature_weather.domain.model.WeatherForecast
import com.example.weatherpulse.feature_weather.presentation.WeatherViewModel
import com.example.weatherpulse.feature_weather.presentation.util.toFormattedDate

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                viewModel.setPermissionsNotGrantedState()
                return@rememberLauncherForActivityResult
            }

            viewModel.fetchWeatherForCurrentLocation()
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            keyboardController?.hide()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearchQueryChange,
            onSearch = { viewModel.fetchWeatherForCity(it) },
            onLocationClick = {
                permissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> {
                    Text(
                        text = state.error ?: "An unknown error occurred.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
                state.todaysWeather == null -> EmptyState()
                else -> {
                    state.todaysWeather?.let {
                        WeatherContent(it, state.weeklyWeather)
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    today: WeatherForecast,
    weekly: List<WeatherForecast>
) {
    Column {
        TodayWeatherSection(today)
        Spacer(modifier = Modifier.height(24.dp))
        if (weekly.isNotEmpty()) {
            Text(
                text = "Weekly Forecast",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            WeeklyForecastList(weekly.drop(1))
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Search for a city to get the latest weather forecast.",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onLocationClick) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Get Current Location"
            )
        }

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
            WeatherIcon(iconCode = weather.icon, size = 100.dp)
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weather.date.toFormattedDate(),
                modifier = Modifier.weight(1.5f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${weather.temp.toInt()}°C",
                modifier = Modifier.weight(0.8f),
                style = MaterialTheme.typography.bodyLarge
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherIcon(iconCode = weather.icon, size = 50.dp)
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
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

@Composable
fun WeatherIcon(iconCode: String, size: Dp) {
    AsyncImage(
        model = "https://openweathermap.org/img/wn/$iconCode@4x.png",
        contentDescription = "Weather Icon",
        modifier = Modifier.size(size)
    )
}
