package com.payu.finance.common

/**
 * A generic class that holds a value or an exception
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension function to convert Result to Resource for UI layer
 */
fun <T> Result<T>.toResource(): Resource<T> {
    return when (this) {
        is Result.Success -> Resource.Success(this.data)
        is Result.Error -> Resource.Error(this.exception.message ?: "Unknown error")
        is Result.Loading -> Resource.Loading()
    }
}

