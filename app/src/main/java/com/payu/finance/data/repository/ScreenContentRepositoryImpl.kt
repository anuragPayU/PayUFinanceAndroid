package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.ScreenContentRemoteDataSource
import com.payu.finance.data.model.toDomain
import com.payu.finance.domain.model.MobileInputScreenContent
import com.payu.finance.domain.model.OtpScreenContent
import com.payu.finance.domain.repository.ScreenContentRepository

/**
 * Implementation of ScreenContentRepository
 * Maps data layer (DTO) to domain layer
 */
class ScreenContentRepositoryImpl(
    private val screenContentRemoteDataSource: ScreenContentRemoteDataSource
) : ScreenContentRepository {
    
    override suspend fun getMobileInputScreenContent(): Result<MobileInputScreenContent> {
        return safeApiCall {
            screenContentRemoteDataSource.getMobileInputScreenContent().toDomain()
        }
    }
    
    override suspend fun getOtpScreenContent(): Result<OtpScreenContent> {
        return safeApiCall {
            screenContentRemoteDataSource.getOtpScreenContent().toDomain()
        }
    }
}

