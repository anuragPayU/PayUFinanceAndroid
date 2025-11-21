package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.AuthRemoteDataSource
import com.payu.finance.data.model.SendOtpRequestDto
import com.payu.finance.data.model.VerifyOtpRequestDto
import com.payu.finance.domain.model.AuthenticateResponse
import com.payu.finance.domain.model.AuthRequest
import com.payu.finance.domain.model.AuthResponse
import com.payu.finance.domain.model.OtpRequest
import com.payu.finance.domain.model.OtpResponse
import com.payu.finance.domain.repository.AuthRepository

/**
 * Implementation of AuthRepository
 * Maps data layer to domain layer
 */
class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendOtp(request: AuthRequest): Result<AuthResponse> {
        return safeApiCall {
            val dto = SendOtpRequestDto.fromDomain(request)
            authRemoteDataSource.sendOtp(dto).toDomain()
        }
    }

    override suspend fun verifyOtp(request: OtpRequest): Result<OtpResponse> {
        return safeApiCall {
            val dto = VerifyOtpRequestDto.fromDomain(request)
            authRemoteDataSource.verifyOtp(dto).toDomain()
        }
    }
    
    override suspend fun authenticate(): Result<AuthenticateResponse> {
        return safeApiCall {
            authRemoteDataSource.authenticate().toDomain()
        }
    }
}

