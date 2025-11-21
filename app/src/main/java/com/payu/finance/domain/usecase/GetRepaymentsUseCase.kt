package com.payu.finance.domain.usecase

import com.payu.finance.common.Result
import com.payu.finance.domain.model.Repayment
import com.payu.finance.domain.repository.RepaymentRepository

/**
 * Use case for getting repayments
 * Can filter by loanId if provided
 */
class GetRepaymentsUseCase(
    private val repaymentRepository: RepaymentRepository
) {
    suspend operator fun invoke(loanId: String? = null): Result<List<Repayment>> {
        return repaymentRepository.getRepayments(loanId)
    }
}

