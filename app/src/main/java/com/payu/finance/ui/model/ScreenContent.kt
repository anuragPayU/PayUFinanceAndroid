package com.payu.finance.ui.model

/**
 * UI Data Model for Mobile Input Screen
 * This is what the View (Composable) uses
 */
data class EnterMobileScreenData(
    val title: String,
    val subtitle: String,
    val mobileNumberLabel: String,
    val mobileNumberPlaceholder: String,
    val continueButtonText: String,
    val consentText: ConsentTextData,
    val errorMessages: ErrorMessagesData,
    val countryCode: String = "+91"
)

/**
 * UI Data Model for Consent Text
 */
data class ConsentTextData(
    val prefixText: String,
    val privacyPolicyLinkText: String,
    val middleText: String,
    val termsAndConditionsLinkText: String,
    val privacyPolicyUrl: String,
    val termsAndConditionsUrl: String
)

/**
 * UI Data Model for Error Messages
 */
data class ErrorMessagesData(
    val invalidMobileNumber: String,
    val networkError: String,
    val genericError: String
)

/**
 * UI Data Model for OTP Screen
 */
data class OtpScreenData(
    val title: String,
    val subtitlePrefix: String,
    val verifyingButtonText: String,
    val subtitleSuffix: String,
    val verifyButtonText: String,
    val resendOtpPrefix: String,
    val resendOtpLinkText: String,
    val errorMessages: OtpErrorMessagesData
)

/**
 * UI Data Model for OTP Error Messages
 */
data class OtpErrorMessagesData(
    val incompleteOtp: String,
    val invalidOtp: String,
    val networkError: String,
    val genericError: String
)
