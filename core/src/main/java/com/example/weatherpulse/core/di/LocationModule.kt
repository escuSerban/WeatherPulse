package com.example.weatherpulse.core.di

import android.app.Application
import com.example.weatherpulse.core.location.LocationTracker
import com.example.weatherpulse.core.location.LocationTrackerImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(locationTrackerImpl: LocationTrackerImpl): LocationTracker

    companion object {
        @Provides
        @Singleton
        fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(app)
        }
    }
}
