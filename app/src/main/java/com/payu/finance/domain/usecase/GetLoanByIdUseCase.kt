package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.Loan
import com.payu.finance.domain.repository.LoanRepository

/**
 * Use case for getting a loan by ID
 */
class GetLoanByIdUseCase(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(loanId: String): Result<Loan> {
        return loanRepository.getLoanById(loanId)
    }
}

