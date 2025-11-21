package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.Loan
import com.payu.finance.domain.repository.LoanRepository

/**
 * Use case for getting all loans
 * Use cases contain business logic and orchestrate repository calls
 */
class GetLoansUseCase(
    private val loanRepository: LoanRepository
) {
    suspend operator fun invoke(): Result<List<Loan>> {
        return loanRepository.getLoans()
    }
}

