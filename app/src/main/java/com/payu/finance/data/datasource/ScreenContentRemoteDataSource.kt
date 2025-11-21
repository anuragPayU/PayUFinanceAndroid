package com.payu.finance.data.datasource

import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.model.ConsentTextDto
import com.payu.finance.data.model.ErrorMessagesDto
import com.payu.finance.data.model.MobileInputScreenContentDto
import com.payu.finance.data.model.OtpErrorMessagesDto
import com.payu.finance.data.model.OtpScreenContentDto
import com.payu.finance.data.model.ProfileScreenDto
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
    
    /**
     * Get Profile Screen Content
     */
    suspend fun getProfileScreenContent(): ProfileScreenDto {
        val response = apiService.getProfileScreenContent()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch profile screen content: ${response.message()}")
        }
        
        // Fallback mock data (for testing/development)
        /*
        return ProfileScreenDto(
            screen = com.payu.finance.data.model.ScreenDto(
                title = "Hi, Rahul",
                subtitle = "+91 9988776655·",
                description = "KYC Verified",
                meta = com.payu.finance.data.model.MetaDto(
                    appVersionLabel = "App Version: 7.6.6"
                ),
                actions = com.payu.finance.data.model.ActionsDto(
                    primary = com.payu.finance.data.model.ActionItemDto(
                        text = "Help",
                        type = "WEBLINK",
                        url = "help url"
                    )
                ),
                sections = com.payu.finance.data.model.SectionsDto(
                    items = listOf(
                        com.payu.finance.data.model.SectionItemDto(
                            type = "list",
                            className = "section",
                            components = com.payu.finance.data.model.ComponentsDto(
                                items = listOf(
                                    com.payu.finance.data.model.ComponentItemDto(
                                        type = "actionable_card",
                                        title = "Privacy policy",
                                        actions = com.payu.finance.data.model.ActionsDto(
                                            default = com.payu.finance.data.model.ActionItemDto(
                                                text = "",
                                                type = "WEBLINK",
                                                url = "web url"
                                            )
                                        ),
                                        assets = com.payu.finance.data.model.AssetsDto(
                                            leadingIcon = "",
                                            trailingIcon = ""
                                        )
                                    ),
                                    com.payu.finance.data.model.ComponentItemDto(
                                        type = "actionable_card",
                                        title = "DPO Terms & Conditions",
                                        actions = com.payu.finance.data.model.ActionsDto(
                                            default = com.payu.finance.data.model.ActionItemDto(
                                                text = "",
                                                type = "WEBLINK",
                                                url = "web url"
                                            )
                                        ),
                                        assets = com.payu.finance.data.model.AssetsDto(
                                            leadingIcon = "",
                                            trailingIcon = ""
                                        )
                                    ),
                                    com.payu.finance.data.model.ComponentItemDto(
                                        type = "actionable_card",
                                        title = "Terms & Conditions",
                                        actions = com.payu.finance.data.model.ActionsDto(
                                            default = com.payu.finance.data.model.ActionItemDto(
                                                text = "",
                                                type = "WEBLINK",
                                                url = "web url"
                                            )
                                        ),
                                        assets = com.payu.finance.data.model.AssetsDto(
                                            leadingIcon = "",
                                            trailingIcon = ""
                                        )
                                    ),
                                    com.payu.finance.data.model.ComponentItemDto(
                                        type = "actionable_card",
                                        title = "Grievance redressal policy",
                                        actions = com.payu.finance.data.model.ActionsDto(
                                            default = com.payu.finance.data.model.ActionItemDto(
                                                text = "",
                                                type = "WEBLINK",
                                                url = "web url"
                                            )
                                        ),
                                        assets = com.payu.finance.data.model.AssetsDto(
                                            leadingIcon = "",
                                            trailingIcon = ""
                                        )
                                    )
                                )
                            )
                        ),
                        com.payu.finance.data.model.SectionItemDto(
                            type = "list",
                            className = "section",
                            components = com.payu.finance.data.model.ComponentsDto(
                                items = listOf(
                                    com.payu.finance.data.model.ComponentItemDto(
                                        type = "actionable_card",
                                        title = "Sign out",
                                        actions = com.payu.finance.data.model.ActionsDto(
                                            default = com.payu.finance.data.model.ActionItemDto(
                                                text = "",
                                                type = "bottomSheet",
                                                url = "url"
                                            )
                                        ),
                                        assets = com.payu.finance.data.model.AssetsDto(
                                            leadingIcon = "",
                                            trailingIcon = ""
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                type = "screen"
            ),
            subScreen = com.payu.finance.data.model.SubScreenDto(
                bottomSheet = com.payu.finance.data.model.BottomSheetDto(
                    title = "Logout?",
                    subtitle = "You won't be able to get transaction notifications after logging out. Are you sure you want to log out?"
                ),
                actions = com.payu.finance.data.model.ActionsDto(
                    primary = com.payu.finance.data.model.ActionItemDto(
                        text = "Logout",
                        type = "logout",
                        url = null
                    ),
                    secondary = com.payu.finance.data.model.ActionItemDto(
                        text = "Stay",
                        type = "DISMISS",
                        url = null
                    )
                )
            )
        )
        */
    }
}

