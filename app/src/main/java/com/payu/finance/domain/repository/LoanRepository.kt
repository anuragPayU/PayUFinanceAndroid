package com.payu.finance.domain.repository

import com.payu.finance.common.Result
import com.payu.finance.domain.model.Loan
import com.payu.finance.domain.model.LoanDetailScreenContent

/**
 * Repository interface for Loan operations
 * This defines the contract for data operations
 */
interface LoanRepository : BaseRepository {
    suspend fun getLoans(): Result<List<Loan>>
    suspend fun getLoanById(loanId: String): Result<Loan>
    suspend fun getLoanDetailScreenContent(loanId: String): Result<LoanDetailScreenContent>
    suspend fun createLoan(loan: Loan): Result<Loan>
}

