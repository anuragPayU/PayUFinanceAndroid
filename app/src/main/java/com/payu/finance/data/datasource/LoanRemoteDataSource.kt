package com.payu.finance.data.datasource

import com.payu.finance.data.api.LoanApiService
import com.payu.finance.data.model.LoanDto

/**
 * Remote data source for Loan
 * Handles API calls and error handling
 */
class LoanRemoteDataSource(
    private val loanApiService: LoanApiService
) {
    suspend fun getLoans(): List<LoanDto> {
        val response = loanApiService.getLoans()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch loans: ${response.message()}")
        }
    }

    suspend fun getLoanById(loanId: String): LoanDto {
        val response = loanApiService.getLoanById(loanId)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch loan: ${response.message()}")
        }
    }

    suspend fun createLoan(loan: LoanDto): LoanDto {
        val response = loanApiService.createLoan(loan)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to create loan: ${response.message()}")
        }
    }
}

