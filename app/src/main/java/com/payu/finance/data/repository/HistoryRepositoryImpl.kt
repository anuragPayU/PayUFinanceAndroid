package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.HistoryRemoteDataSource
import com.payu.finance.data.model.toDomain
import com.payu.finance.domain.model.HistoryScreenContent
import com.payu.finance.domain.repository.HistoryRepository

/**
 * Implementation of HistoryRepository
 * Maps data layer (DTO) to domain layer
 */
class HistoryRepositoryImpl(
    private val historyRemoteDataSource: HistoryRemoteDataSource
) : HistoryRepository {
    
    override suspend fun getHistoryScreenContent(): Result<HistoryScreenContent> {
        return safeApiCall {
            historyRemoteDataSource.getHistoryScreenContent().toDomain()
        }
    }
}

