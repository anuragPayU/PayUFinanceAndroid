package com.payu.finance.domain.usecase

import com.payu.finance.domain.model.HistoryScreenContent
import com.payu.finance.domain.repository.HistoryRepository

/**
 * Use case to get History Screen Content
 */
class GetHistoryScreenContentUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(): com.payu.finance.common.Result<HistoryScreenContent> {
        return historyRepository.getHistoryScreenContent()
    }
}

