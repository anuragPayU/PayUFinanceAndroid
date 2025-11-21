package com.payu.finance.data.api

import com.payu.finance.data.model.HistoryScreenDto
import com.payu.finance.data.model.HomeScreenDto
import com.payu.finance.data.model.MobileInputScreenContentDto
import com.payu.finance.data.model.OtpScreenContentDto
import com.payu.finance.data.model.ProfileScreenDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * API service interface for Screen Content endpoints
 */
interface ScreenContentApiService {
    @GET("screen-content/mobile-input")
    suspend fun getMobileInputScreenContent(): Response<MobileInputScreenContentDto>
    
    @GET("screen-content/otp")
    suspend fun getOtpScreenContent(): Response<OtpScreenContentDto>
    
    @GET("users/repayment-dashboard")
    suspend fun getHomeScreenContent(): Response<HomeScreenDto>
    
    @GET("users/repayment-history")
    suspend fun getHistoryScreenContent(): Response<HistoryScreenDto>
    
    @GET("users/profile")
    suspend fun getProfileScreenContent(): Response<ProfileScreenDto>
}

