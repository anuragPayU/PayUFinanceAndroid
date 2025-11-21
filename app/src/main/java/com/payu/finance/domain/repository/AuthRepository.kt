package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.AuthenticateResponse
import com.payu.finance.domain.model.AuthRequest
import com.payu.finance.domain.model.AuthResponse
import com.payu.finance.domain.model.OtpRequest
import com.payu.finance.domain.model.OtpResponse

/**
 * Repository interface for Authentication operations
 */
interface AuthRepository : BaseRepository {
    suspend fun sendOtp(request: AuthRequest): Result<AuthResponse>
    suspend fun verifyOtp(request: OtpRequest): Result<OtpResponse>
    suspend fun authenticate(): Result<AuthenticateResponse>
}

