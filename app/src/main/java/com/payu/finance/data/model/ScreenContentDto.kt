package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for Mobile Input Screen Content API Response
 */
data class MobileInputScreenContentDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("mobile_number_label")
    val mobileNumberLabel: String,
    @SerializedName("mobile_number_placeholder")
    val mobileNumberPlaceholder: String,
    @SerializedName("continue_button_text")
    val continueButtonText: String,
    @SerializedName("consent_text")
    val consentText: ConsentTextDto,
    @SerializedName("error_messages")
    val errorMessages: ErrorMessagesDto,
    @SerializedName("country_code")
    val countryCode: String = "+91"
)

/**
 * DTO for Consent Text
 */
data class ConsentTextDto(
    @SerializedName("prefix_text")
    val prefixText: String,
    @SerializedName("privacy_policy_link_text")
    val privacyPolicyLinkText: String,
    @SerializedName("middle_text")
    val middleText: String,
    @SerializedName("terms_and_conditions_link_text")
    val termsAndConditionsLinkText: String,
    @SerializedName("privacy_policy_url")
    val privacyPolicyUrl: String,
    @SerializedName("terms_and_conditions_url")
    val termsAndConditionsUrl: String
)

/**
 * DTO for Error Messages
 */
data class ErrorMessagesDto(
    @SerializedName("invalid_mobile_number")
    val invalidMobileNumber: String,
    @SerializedName("network_error")
    val networkError: String,
    @SerializedName("generic_error")
    val genericError: String
)

/**
 * DTO for OTP Screen Content API Response
 */
data class OtpScreenContentDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle_prefix")
    val subtitlePrefix: String,
    @SerializedName("subtitle_suffix")
    val subtitleSuffix: String,
    @SerializedName("verify_button_text")
    val verifyButtonText: String,
    @SerializedName("resend_otp_prefix")
    val resendOtpPrefix: String,
    @SerializedName("resend_otp_link_text")
    val resendOtpLinkText: String,
    @SerializedName("error_messages")
    val errorMessages: OtpErrorMessagesDto
)

/**
 * DTO for OTP Error Messages
 */
data class OtpErrorMessagesDto(
    @SerializedName("incomplete_otp")
    val incompleteOtp: String,
    @SerializedName("invalid_otp")
    val invalidOtp: String,
    @SerializedName("network_error")
    val networkError: String,
    @SerializedName("generic_error")
    val genericError: String
)

/**
 * Mapper: DTO -> Domain
 */
fun MobileInputScreenContentDto.toDomain(): com.payu.finance.domain.model.MobileInputScreenContent {
    return com.payu.finance.domain.model.MobileInputScreenContent(
        title = title,
        subtitle = subtitle,
        mobileNumberLabel = mobileNumberLabel,
        mobileNumberPlaceholder = mobileNumberPlaceholder,
        continueButtonText = continueButtonText,
        consentText = consentText.toDomain(),
        errorMessages = errorMessages.toDomain(),
        countryCode = countryCode
    )
}

fun ConsentTextDto.toDomain(): com.payu.finance.domain.model.ConsentTextContent {
    return com.payu.finance.domain.model.ConsentTextContent(
        prefixText = prefixText,
        privacyPolicyLinkText = privacyPolicyLinkText,
        middleText = middleText,
        termsAndConditionsLinkText = termsAndConditionsLinkText,
        privacyPolicyUrl = privacyPolicyUrl,
        termsAndConditionsUrl = termsAndConditionsUrl
    )
}

fun ErrorMessagesDto.toDomain(): com.payu.finance.domain.model.ErrorMessagesContent {
    return com.payu.finance.domain.model.ErrorMessagesContent(
        invalidMobileNumber = invalidMobileNumber,
        networkError = networkError,
        genericError = genericError
    )
}

fun OtpScreenContentDto.toDomain(): com.payu.finance.domain.model.OtpScreenContent {
    return com.payu.finance.domain.model.OtpScreenContent(
        title = title,
        subtitlePrefix = subtitlePrefix,
        subtitleSuffix = subtitleSuffix,
        verifyButtonText = verifyButtonText,
        resendOtpPrefix = resendOtpPrefix,
        resendOtpLinkText = resendOtpLinkText,
        errorMessages = errorMessages.toDomain()
    )
}

fun OtpErrorMessagesDto.toDomain(): com.payu.finance.domain.model.OtpErrorMessagesContent {
    return com.payu.finance.domain.model.OtpErrorMessagesContent(
        incompleteOtp = incompleteOtp,
        invalidOtp = invalidOtp,
        networkError = networkError,
        genericError = genericError
    )
}

