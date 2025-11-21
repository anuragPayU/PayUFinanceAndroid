package com.payu.finance.data.datasource

import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.model.ConsentTextDto
import com.payu.finance.data.model.ErrorMessagesDto
import com.payu.finance.data.model.MobileInputScreenContentDto
import com.payu.finance.data.model.OtpErrorMessagesDto
import com.payu.finance.data.model.OtpScreenContentDto
import kotlinx.coroutines.delay

/**
 * Remote data source for Screen Content
 * Handles API calls for screen content
 * Currently returns mock data - replace with actual API calls when backend is ready
 */
class ScreenContentRemoteDataSource(
    private val apiService: ScreenContentApiService
) {
    /**
     * Get Mobile Input Screen Content
     * TODO: Replace with actual API call when backend is ready
     */
    suspend fun getMobileInputScreenContent(): MobileInputScreenContentDto {
        // Simulate network delay
        delay(500)
        
        // Return mock data
        return MobileInputScreenContentDto(
            title = "Login in to PayU Finance",
            subtitle = "Track & Repay all your PayU Finance EMIs",
            mobileNumberLabel = "Mobile Number",
            mobileNumberPlaceholder = "Enter 10-digit mobile number",
            continueButtonText = "Continue",
            consentText = ConsentTextDto(
                prefixText = "By continuing, I agree to our ",
                privacyPolicyLinkText = "Privacy Policy",
                middleText = " & ",
                termsAndConditionsLinkText = "T&C",
                privacyPolicyUrl = "https://www.payu.in/privacy-policy",
                termsAndConditionsUrl = "https://www.payu.in/terms-and-conditions"
            ),
            errorMessages = ErrorMessagesDto(
                invalidMobileNumber = "Please enter a valid 10-digit mobile number",
                networkError = "Network error. Please try again.",
                genericError = "An error occurred"
            ),
            countryCode = "+91"
        )
        
        // Uncomment below when backend is ready:
        /*
        val response = apiService.getMobileInputScreenContent()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch mobile input screen content: ${response.message()}")
        }
        */
    }
    
    /**
     * Get OTP Screen Content
     * TODO: Replace with actual API call when backend is ready
     */
    suspend fun getOtpScreenContent(): OtpScreenContentDto {
        // Simulate network delay
        delay(500)
        
        // Return mock data
        return OtpScreenContentDto(
            title = "Enter OTP",
            subtitlePrefix = "We've sent an OTP to ",
            subtitleSuffix = "",
            verifyButtonText = "Verify",
            resendOtpPrefix = "Didn't receive OTP? ",
            resendOtpLinkText = "Resend",
            errorMessages = OtpErrorMessagesDto(
                incompleteOtp = "Please enter complete 6-digit OTP",
                invalidOtp = "Invalid OTP. Please try again.",
                networkError = "Network error. Please try again.",
                genericError = "An error occurred"
            )
        )
        
        // Uncomment below when backend is ready:
        /*
        val response = apiService.getOtpScreenContent()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch OTP screen content: ${response.message()}")
        }
        */
    }
}

