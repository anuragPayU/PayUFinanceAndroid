package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName
import com.payu.finance.domain.model.AuthRequest
import com.payu.finance.domain.model.AuthResponse
import com.payu.finance.domain.model.AuthenticateResponse
import com.payu.finance.domain.model.OtpRequest
import com.payu.finance.domain.model.OtpResponse

/**
 * Data Transfer Object for sending OTP request
 */
data class SendOtpRequestDto(
    @SerializedName("phone")
    val phone: String
) {
    companion object {
        fun fromDomain(request: AuthRequest): SendOtpRequestDto {
            return SendOtpRequestDto(phone = request.mobileNumber)
        }
    }
}

/**
 * Data Transfer Object for sending OTP response
 * API Response: {"phone": "+918779907465", "allow_manual_entry": true, "whatsapp_consent": true}
 */
data class SendOtpResponseDto(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("allow_manual_entry")
    val allowManualEntry: Boolean? = null,
    @SerializedName("whatsapp_consent")
    val whatsappConsent: Boolean? = null
) {
    fun toDomain(): AuthResponse {
        // If we get a response with phone, consider it successful
        val isSuccess = phone != null
        
        return AuthResponse(
            success = isSuccess,
            message = null,
            otpSent = isSuccess,
            allowManualEntry = allowManualEntry ?: false,
            whatsappConsent = whatsappConsent ?: false
        )
    }
}

/**
 * Data Transfer Object for verifying OTP request
 */
data class VerifyOtpRequestDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("phone")
    val phone: String
) {
    companion object {
        fun fromDomain(request: OtpRequest): VerifyOtpRequestDto {
            // Format phone number with +91 prefix
            val phoneWithCountryCode = if (request.mobileNumber.startsWith("+91")) {
                request.mobileNumber
            } else {
                "+91${request.mobileNumber}"
            }
            return VerifyOtpRequestDto(
                code = request.otp,
                phone = phoneWithCountryCode
            )
        }
    }
}

/**
 * Data Transfer Object for verifying OTP response
 * Flexible structure to handle different API response formats
 * Response: {"muid":10304397}
 */
data class VerifyOtpResponseDto(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("muid")
    val muid: Long? = null
) {
    fun toDomain(): OtpResponse {
        // Use accessToken if token is not available, or vice versa
        val authToken = token ?: accessToken
        // If success is explicitly false, use it; otherwise assume success if status code was 200
        val isSuccess = success ?: true
        
        return OtpResponse(
            success = isSuccess,
            message = message,
            token = authToken,
            muid = muid
        )
    }
}

/**
 * Data Transfer Object for authenticate request
 * API expects empty body {}
 */
class AuthenticateRequestDto(
    // Empty class - API expects empty JSON object {}
)

/**
 * Data Transfer Object for authenticate response
 * API Response: {} (empty body, success determined by status code)
 */
data class AuthenticateResponseDto(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("message")
    val message: String? = null
) {
    fun toDomain(): AuthenticateResponse {
        return AuthenticateResponse(
            success = success ?: true,
            message = message
        )
    }
}

