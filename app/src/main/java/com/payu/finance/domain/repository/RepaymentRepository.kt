package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.Repayment

/**
 * Repository interface for Repayment operations
 * This defines the contract for data operations
 */
interface RepaymentRepository : BaseRepository {
    suspend fun getRepayments(loanId: String? = null): Result<List<Repayment>>
    suspend fun getRepaymentById(repaymentId: String): Result<Repayment>
    suspend fun createRepayment(repayment: Repayment): Result<Repayment>
    suspend fun updateRepaymentStatus(repaymentId: String, status: String): Result<Repayment>
}

