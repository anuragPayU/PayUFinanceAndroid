package com.payu.finance.di

import com.payu.finance.domain.usecase.AuthenticateUseCase
import com.payu.finance.domain.usecase.GetHistoryScreenContentUseCase
import com.payu.finance.domain.usecase.GetHomeScreenContentUseCase
import com.payu.finance.domain.usecase.GetLoanByIdUseCase
import com.payu.finance.domain.usecase.GetLoanDetailScreenContentUseCase
import com.payu.finance.domain.usecase.GetLoansUseCase
import com.payu.finance.domain.usecase.GetProfileScreenContentUseCase
import com.payu.finance.domain.usecase.GetRepaymentsUseCase
import com.payu.finance.domain.usecase.SendOtpUseCase
import com.payu.finance.domain.usecase.VerifyOtpUseCase
import com.payu.finance.ui.viewmodel.HistoryViewModel
import com.payu.finance.ui.viewmodel.HomeViewModel
import com.payu.finance.ui.viewmodel.LoanDetailViewModel
import com.payu.finance.ui.viewmodel.LoansViewModel
import com.payu.finance.ui.viewmodel.MobileInputViewModel
import com.payu.finance.ui.viewmodel.OtpViewModel
import com.payu.finance.ui.viewmodel.ProfileViewModel
import com.payu.finance.ui.viewmodel.RepaymentViewModel
import com.payu.finance.ui.viewmodel.RepaymentsViewModel
import com.payu.finance.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * ViewModel module for dependency injection
 * Provides ViewModels
 */
val viewModelModule = module {
    viewModel { HomeViewModel(get<GetLoansUseCase>(), get<GetRepaymentsUseCase>(), get<GetHomeScreenContentUseCase>()) }
    viewModel { LoansViewModel(get<GetLoansUseCase>()) }
    viewModel { RepaymentsViewModel(get<GetRepaymentsUseCase>()) }
    viewModel { RepaymentViewModel() }
    viewModel { LoanDetailViewModel(get<GetLoanDetailScreenContentUseCase>()) }
    viewModel { HistoryViewModel(get<GetHistoryScreenContentUseCase>()) }
    viewModel { ProfileViewModel(get<GetProfileScreenContentUseCase>()) }
    viewModel { 
        MobileInputViewModel(
            get<SendOtpUseCase>(),
            get<com.payu.finance.domain.repository.ScreenContentRepository>()
        ) 
    }
    viewModel {
        OtpViewModel(
            get<VerifyOtpUseCase>(),
            get<SendOtpUseCase>(),
            get<com.payu.finance.domain.repository.ScreenContentRepository>()
        )
    }
    viewModel { SplashViewModel(get<AuthenticateUseCase>()) }
}

