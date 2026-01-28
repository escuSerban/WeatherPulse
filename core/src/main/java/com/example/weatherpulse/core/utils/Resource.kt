package com.example.weatherpulse.core.utils

/**
 * A generic sealed class that wraps data with its loading status.
 * Used for propagating network state and data through different layers.
 */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}
