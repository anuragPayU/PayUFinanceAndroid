package com.payu.finance.domain.usecase

import com.payu.finance.domain.model.HomeScreenContent
import com.payu.finance.domain.repository.HomeRepository

/**
 * Use case to get Home Screen Content
 */
class GetHomeScreenContentUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): com.payu.finance.common.Result<HomeScreenContent> {
        return homeRepository.getHomeScreenContent()
    }
}

