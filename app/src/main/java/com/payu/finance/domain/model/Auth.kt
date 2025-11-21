package com.payu.finance.domain.model

/**
 * Domain model for authentication request
 */
data class AuthRequest(
    val mobileNumber: String
)

/**
 * Domain model for authentication response
 */
data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val otpSent: Boolean = false,
    val allowManualEntry: Boolean = false,
    val whatsappConsent: Boolean = false
)

/**
 * Domain model for OTP verification request
 */
data class OtpRequest(
    val mobileNumber: String,
    val otp: String
)

/**
 * Domain model for OTP verification response
 */
data class OtpResponse(
    val success: Boolean,
    val message: String?,
    val token: String? = null
)

