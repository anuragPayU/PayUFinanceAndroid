package com.payu.finance.data.network

import com.payu.finance.data.preferences.PreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add cookies to API requests
 * Excludes Login and Verify OTP endpoints as they set the cookies
 */
class CookieInterceptor(
    private val preferencesManager: PreferencesManager
) : Interceptor {
    
    companion object {
        // Endpoints that should NOT have cookies added
        private val EXCLUDED_ENDPOINTS = listOf(
            "users/login",
            "users/otp"
        )
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()
        
        // Check if this endpoint should have cookies added
        // Exclude login and otp endpoints, but include authenticate endpoint
        // (authenticate needs cookies to validate them)
        val isExcludedEndpoint = EXCLUDED_ENDPOINTS.any { endpoint ->
            url.contains(endpoint)
        }
        
        val requestBuilder = originalRequest.newBuilder()
        
        // Add cookies to all endpoints except login and otp
        if (!isExcludedEndpoint) {
            val cookies = preferencesManager.getCookies()
            if (cookies.isNotEmpty()) {
                // Add cookies to the Cookie header
                requestBuilder.header("Cookie", cookies)
            }
        }
        
        return chain.proceed(requestBuilder.build())
    }
}

