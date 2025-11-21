package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.AuthenticateResponse
import com.payu.finance.domain.repository.AuthRepository

/**
 * Use case for authenticating user with stored cookies
 */
class AuthenticateUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<AuthenticateResponse> {
        return authRepository.authenticate()
    }
}

