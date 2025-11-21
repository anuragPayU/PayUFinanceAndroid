package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.LoanDetailScreenContent
import com.payu.finance.domain.repository.LoanRepository

/**
 * Use case to get Loan Detail Screen Content
 */
class GetLoanDetailScreenContentUseCase(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanId: String): Result<LoanDetailScreenContent> {
        return loanRepository.getLoanDetailScreenContent(loanId)
    }
}

