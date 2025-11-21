package com.payu.finance.ui.model

import com.payu.finance.domain.model.ConsentTextContent
import com.payu.finance.domain.model.ErrorMessagesContent
import com.payu.finance.domain.model.MobileInputScreenContent
import com.payu.finance.domain.model.OtpErrorMessagesContent
import com.payu.finance.domain.model.OtpScreenContent

/**
 * Mapper: Domain -> UI Model
 * Maps domain models to UI data models that Views use
 */
fun MobileInputScreenContent.toUiModel(): EnterMobileScreenData {
    return EnterMobileScreenData(
        title = title,
        subtitle = subtitle,
        mobileNumberLabel = mobileNumberLabel,
        mobileNumberPlaceholder = mobileNumberPlaceholder,
        continueButtonText = continueButtonText,
        consentText = consentText.toUiModel(),
        errorMessages = errorMessages.toUiModel(),
        countryCode = countryCode
    )
}

fun ConsentTextContent.toUiModel(): ConsentTextData {
    return ConsentTextData(
        prefixText = prefixText,
        privacyPolicyLinkText = privacyPolicyLinkText,
        middleText = middleText,
        termsAndConditionsLinkText = termsAndConditionsLinkText,
        privacyPolicyUrl = privacyPolicyUrl,
        termsAndConditionsUrl = termsAndConditionsUrl
    )
}

fun ErrorMessagesContent.toUiModel(): ErrorMessagesData {
    return ErrorMessagesData(
        invalidMobileNumber = invalidMobileNumber,
        networkError = networkError,
        genericError = genericError
    )
}

fun OtpScreenContent.toUiModel(): OtpScreenData {
    return OtpScreenData(
        title = title,
        subtitlePrefix = subtitlePrefix,
        subtitleSuffix = subtitleSuffix,
        verifyButtonText = verifyButtonText,
        resendOtpPrefix = resendOtpPrefix,
        resendOtpLinkText = resendOtpLinkText,
        verifyingButtonText = "Verifying OTP...",
        errorMessages = errorMessages.toUiModel()
    )
}

fun OtpErrorMessagesContent.toUiModel(): OtpErrorMessagesData {
    return OtpErrorMessagesData(
        incompleteOtp = incompleteOtp,
        invalidOtp = invalidOtp,
        networkError = networkError,
        genericError = genericError
    )
}

