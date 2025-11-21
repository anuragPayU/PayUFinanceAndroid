package com.payu.finance.domain.repository

import com.payu.finance.common.Result

/**
 * Base repository interface
 * All repositories should implement common error handling patterns
 */
interface BaseRepository {
    /**
     * Handle API errors and convert to Result
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.Success(apiCall())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

