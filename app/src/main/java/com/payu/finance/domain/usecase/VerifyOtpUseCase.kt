package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.OtpRequest
import com.payu.finance.domain.model.OtpResponse
import com.payu.finance.domain.repository.AuthRepository

/**
 * Use case for verifying OTP
 */
class VerifyOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(mobileNumber: String, otp: String): Result<OtpResponse> {
        return authRepository.verifyOtp(OtpRequest(mobileNumber = mobileNumber, otp = otp))
    }
}

