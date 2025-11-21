package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.HomeRemoteDataSource
import com.payu.finance.data.model.toDomain
import com.payu.finance.domain.model.HomeScreenContent
import com.payu.finance.domain.repository.HomeRepository

/**
 * Implementation of HomeRepository
 * Maps data layer (DTO) to domain layer
 */
class HomeRepositoryImpl(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    
    override suspend fun getHomeScreenContent(): Result<HomeScreenContent> {
        return safeApiCall {
            homeRemoteDataSource.getHomeScreenContent().toDomain()
        }
    }
}

