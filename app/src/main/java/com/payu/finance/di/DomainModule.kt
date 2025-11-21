package com.payu.finance.di

import com.payu.finance.domain.repository.AuthRepository
import com.payu.finance.domain.repository.HomeRepository
import com.payu.finance.domain.repository.LoanRepository
import com.payu.finance.domain.repository.RepaymentRepository
import com.payu.finance.domain.usecase.GetHomeScreenContentUseCase
import com.payu.finance.domain.usecase.GetLoanByIdUseCase
import com.payu.finance.domain.usecase.GetLoansUseCase
import com.payu.finance.domain.usecase.GetRepaymentsUseCase
import com.payu.finance.domain.usecase.SendOtpUseCase
import com.payu.finance.domain.usecase.VerifyOtpUseCase
import org.koin.dsl.module

/**
 * Domain module for dependency injection
 * Provides use cases
 */
val domainModule = module {
    // Use Cases
    factory { GetLoansUseCase(get<LoanRepository>()) }
    factory { GetLoanByIdUseCase(get<LoanRepository>()) }
    factory { GetRepaymentsUseCase(get<RepaymentRepository>()) }
    factory { GetHomeScreenContentUseCase(get<HomeRepository>()) }
    factory { SendOtpUseCase(get<AuthRepository>()) }
    factory { VerifyOtpUseCase(get<AuthRepository>()) }
}

