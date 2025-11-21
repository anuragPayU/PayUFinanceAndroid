package com.payu.finance.data.datasource

import com.payu.finance.data.api.AuthApiService
import com.payu.finance.data.model.AuthenticateResponseDto
import com.payu.finance.data.model.SendOtpRequestDto
import com.payu.finance.data.model.SendOtpResponseDto
import com.payu.finance.data.model.VerifyOtpRequestDto
import com.payu.finance.data.model.VerifyOtpResponseDto
import com.payu.finance.data.preferences.PreferencesManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Remote data source for Authentication
 * Handles API calls and error handling
 */
class AuthRemoteDataSource(
    private val authApiService: AuthApiService,
    private val preferencesManager: PreferencesManager
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
            val responseDto = response.body()!!
            
            // Extract cookies from response headers
            // OkHttp headers are case-insensitive, so "Set-Cookie" will match "set-cookie"
            val setCookieHeaders = response.headers().values("Set-Cookie")
            if (setCookieHeaders.isNotEmpty()) {
                // Combine all Set-Cookie headers into a single cookie string
                // Format: "cookie1=value1; cookie2=value2"
                val cookies = setCookieHeaders.joinToString("; ") { cookieHeader ->
                    // Extract cookie name and value (before semicolon)
                    cookieHeader.split(";").first().trim()
                }
                preferencesManager.saveCookies(cookies)
            }
            
            // Save muid if present
            responseDto.muid?.let { muid ->
                preferencesManager.saveMuid(muid)
            }
            
            return responseDto
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: response.message()
            throw Exception("Failed to verify OTP: $errorMessage")
        }
    }
    
    suspend fun authenticate(): AuthenticateResponseDto {
        // Empty body {} as per API requirement
        // Create empty JSON object as RequestBody
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = "{}".toRequestBody(jsonMediaType)
        
        // Make the API call - this will trigger CookieInterceptor to add cookies
        // CookieInterceptor will automatically add cookies from PreferencesManager
        val response = authApiService.authenticate(requestBody)
        
        if (response.isSuccessful) {
            // API returns empty body {} on success (200 OK), so we create a success response
            val responseBody = response.body()
            return responseBody ?: AuthenticateResponseDto(success = true, message = null)
        } else {
            // API call failed - cookies might be invalid/expired or no cookies present
            val errorBody = response.errorBody()?.string()
            val errorMessage = errorBody ?: response.message()
            throw Exception("Authentication failed: $errorMessage")
        }
    }
}

