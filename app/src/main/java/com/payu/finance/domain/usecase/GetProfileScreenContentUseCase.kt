package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.ProfileScreenContent
import com.payu.finance.domain.repository.ScreenContentRepository

/**
 * Use case to get Profile Screen Content
 */
class GetProfileScreenContentUseCase(
    private val screenContentRepository: ScreenContentRepository
) {
    suspend operator fun invoke(): Result<ProfileScreenContent> {
        return screenContentRepository.getProfileScreenContent()
    }
}

