package com.payu.finance.common

/**
 * A generic class that holds a value with its loading state.
 * Used for UI state management in ViewModels
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    
    companion object {
        fun <T> Loading(): Resource<T> = Loading(null)
    }
}

