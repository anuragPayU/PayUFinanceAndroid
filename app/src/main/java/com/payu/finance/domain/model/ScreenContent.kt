package com.payu.finance.domain.model

/**
 * Domain model for Mobile Input Screen Content
 */
data class MobileInputScreenContent(
    val title: String,
    val subtitle: String,
    val mobileNumberLabel: String,
    val mobileNumberPlaceholder: String,
    val continueButtonText: String,
    val consentText: ConsentTextContent,
    val errorMessages: ErrorMessagesContent,
    val countryCode: String = "+91"
)

/**
 * Domain model for Consent Text
 */
data class ConsentTextContent(
    val prefixText: String,
    val privacyPolicyLinkText: String,
    val middleText: String,
    val termsAndConditionsLinkText: String,
    val privacyPolicyUrl: String,
    val termsAndConditionsUrl: String
)

/**
 * Domain model for Error Messages
 */
data class ErrorMessagesContent(
    val invalidMobileNumber: String,
    val networkError: String,
    val genericError: String
)

/**
 * Domain model for OTP Screen Content
 */
data class OtpScreenContent(
    val title: String,
    val subtitlePrefix: String,
    val subtitleSuffix: String,
    val verifyButtonText: String,
    val resendOtpPrefix: String,
    val resendOtpLinkText: String,
    val errorMessages: OtpErrorMessagesContent
)

/**
 * Domain model for OTP Error Messages
 */
data class OtpErrorMessagesContent(
    val incompleteOtp: String,
    val invalidOtp: String,
    val networkError: String,
    val genericError: String
)

