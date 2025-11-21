package com.payu.finance.data.api

import com.payu.finance.data.model.SendOtpRequestDto
import com.payu.finance.data.model.SendOtpResponseDto
import com.payu.finance.data.model.VerifyOtpRequestDto
import com.payu.finance.data.model.VerifyOtpResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service interface for Authentication endpoints
 */
interface AuthApiService {
    @POST("users/otp")
    suspend fun sendOtp(@Body request: SendOtpRequestDto): Response<SendOtpResponseDto>

    @POST("users/login")
    suspend fun verifyOtp(@Body request: VerifyOtpRequestDto): Response<VerifyOtpResponseDto>
}

