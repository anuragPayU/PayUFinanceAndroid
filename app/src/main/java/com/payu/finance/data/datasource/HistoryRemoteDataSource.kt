package com.payu.finance.data.datasource

import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.model.*
import kotlinx.coroutines.delay

/**
 * Remote data source for History Screen
 * Handles API calls and error handling
 * Currently returns mock data - replace with actual API calls when backend is ready
 */
class HistoryRemoteDataSource(
    private val screenContentApiService: ScreenContentApiService
) {
    /**
     * Get History Screen Content from API
     */
    suspend fun getHistoryScreenContent(): HistoryScreenDto {
        val response = screenContentApiService.getHistoryScreenContent()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch history screen: ${response.message()}")
        }
    }
}

