package com.example.weatherpulse.feature_weather.di

import com.example.weatherpulse.feature_weather.data.remote.WeatherApi
import com.example.weatherpulse.feature_weather.data.repository.WeatherRepositoryImpl
import com.example.weatherpulse.feature_weather.domain.repository.WeatherRepository
import com.example.weatherpulse.feature_weather.domain.usecase.GetWeather
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGetWeather(repository: WeatherRepository): GetWeather {
        return GetWeather(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}
