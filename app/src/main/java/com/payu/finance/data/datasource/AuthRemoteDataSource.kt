package com.payu.finance.data.datasource

import com.payu.finance.data.api.AuthApiService
import com.payu.finance.data.model.SendOtpRequestDto
import com.payu.finance.data.model.SendOtpResponseDto
import com.payu.finance.data.model.VerifyOtpRequestDto
import com.payu.finance.data.model.VerifyOtpResponseDto

/**
 * Remote data source for Authentication
 * Handles API calls and error handling
 */
class AuthRemoteDataSource(
    private val authApiService: AuthApiService
) {
    suspend fun sendOtp(request: SendOtpRequestDto): SendOtpResponseDto {
        val response = authApiService.sendOtp(request)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: response.message()
            throw Exception("Failed to send OTP: $errorMessage")
        }
    }

    suspend fun verifyOtp(request: VerifyOtpRequestDto): VerifyOtpResponseDto {
        val response = authApiService.verifyOtp(request)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: response.message()
            throw Exception("Failed to verify OTP: $errorMessage")
        }
    }
}

