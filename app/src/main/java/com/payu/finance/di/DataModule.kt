package com.payu.finance.di

import com.payu.finance.data.api.AuthApiService
import com.payu.finance.data.api.LoanApiService
import com.payu.finance.data.api.RepaymentApiService
import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.datasource.AuthRemoteDataSource
import com.payu.finance.data.datasource.HomeRemoteDataSource
import com.payu.finance.data.datasource.LoanRemoteDataSource
import com.payu.finance.data.datasource.RepaymentRemoteDataSource
import com.payu.finance.data.datasource.ScreenContentRemoteDataSource
import com.payu.finance.data.repository.AuthRepositoryImpl
import com.payu.finance.data.repository.HomeRepositoryImpl
import com.payu.finance.data.repository.LoanRepositoryImpl
import com.payu.finance.data.repository.RepaymentRepositoryImpl
import com.payu.finance.data.repository.ScreenContentRepositoryImpl
import com.payu.finance.domain.repository.AuthRepository
import com.payu.finance.domain.repository.HomeRepository
import com.payu.finance.domain.repository.LoanRepository
import com.payu.finance.domain.repository.RepaymentRepository
import com.payu.finance.domain.repository.ScreenContentRepository
import org.koin.dsl.module

/**
 * Data module for dependency injection
 * Provides data sources and repository implementations
 */
val dataModule = module {
    // Data Sources
    single<LoanRemoteDataSource> {
        LoanRemoteDataSource(get<LoanApiService>())
    }

    single<RepaymentRemoteDataSource> {
        RepaymentRemoteDataSource(get<RepaymentApiService>())
    }

    single<AuthRemoteDataSource> {
        AuthRemoteDataSource(get<AuthApiService>())
    }
    
    single<ScreenContentRemoteDataSource> {
        ScreenContentRemoteDataSource(get<ScreenContentApiService>())
    }
    
    single<HomeRemoteDataSource> {
        HomeRemoteDataSource(get<ScreenContentApiService>())
    }

    // Repositories
    single<LoanRepository> {
        LoanRepositoryImpl(get<LoanRemoteDataSource>())
    }

    single<RepaymentRepository> {
        RepaymentRepositoryImpl(get<RepaymentRemoteDataSource>())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(get<AuthRemoteDataSource>())
    }
    
    single<ScreenContentRepository> {
        ScreenContentRepositoryImpl(get<ScreenContentRemoteDataSource>())
    }
    
    single<HomeRepository> {
        HomeRepositoryImpl(get<HomeRemoteDataSource>())
    }
}

