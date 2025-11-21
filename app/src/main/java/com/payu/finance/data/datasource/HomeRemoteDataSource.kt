package com.payu.finance.data.datasource

import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.model.HomeScreenDto

/**
 * Remote data source for Home Screen
 * Handles API calls and error handling
 */
class HomeRemoteDataSource(
    private val screenContentApiService: ScreenContentApiService
) {
    /**
     * Get Home Screen Content from API
     * Calls /users/repayment-dashboard endpoint
     */
    suspend fun getHomeScreenContent(): HomeScreenDto {
        val response = screenContentApiService.getHomeScreenContent()
        
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: response.message()
            throw Exception("Failed to fetch home screen: $errorMessage")
        }
    }
}

