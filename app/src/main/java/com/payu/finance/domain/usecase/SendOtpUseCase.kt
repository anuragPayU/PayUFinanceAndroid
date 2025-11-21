package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.AuthRequest
import com.payu.finance.domain.model.AuthResponse
import com.payu.finance.domain.repository.AuthRepository

/**
 * Use case for sending OTP to mobile number
 */
class SendOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(mobileNumber: String): Result<AuthResponse> {
        return authRepository.sendOtp(AuthRequest(mobileNumber = mobileNumber))
    }
}

