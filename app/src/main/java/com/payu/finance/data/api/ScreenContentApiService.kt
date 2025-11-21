package com.payu.finance.data.api

import com.payu.finance.data.model.HomeScreenDto
import com.payu.finance.data.model.MobileInputScreenContentDto
import com.payu.finance.data.model.OtpScreenContentDto
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
    
    @GET("screen-content/home")
    suspend fun getHomeScreenContent(): Response<HomeScreenDto>
}

