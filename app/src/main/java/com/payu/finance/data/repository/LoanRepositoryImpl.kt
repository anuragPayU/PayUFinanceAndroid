package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.LoanRemoteDataSource
import com.payu.finance.domain.model.Loan
import com.payu.finance.domain.model.LoanDetailScreenContent
import com.payu.finance.domain.repository.LoanRepository

/**
 * Implementation of LoanRepository
 * Maps data layer to domain layer
 */
class LoanRepositoryImpl(
    private val loanRemoteDataSource: LoanRemoteDataSource
) : LoanRepository {

    override suspend fun getLoans(): Result<List<Loan>> {
        return safeApiCall {
            loanRemoteDataSource.getLoans().map { it.toDomain() }
        }
    }

    override suspend fun getLoanById(loanId: String): Result<Loan> {
        return safeApiCall {
            loanRemoteDataSource.getLoanById(loanId).toDomain()
        }
    }

    override suspend fun getLoanDetailScreenContent(loanId: String): Result<LoanDetailScreenContent> {
        return safeApiCall {
            loanRemoteDataSource.getLoanDetailScreenContent(loanId).toDomain()
        }
    }

    override suspend fun createLoan(loan: Loan): Result<Loan> {
        return safeApiCall {
            // Convert domain model to DTO
            val loanDto = com.payu.finance.data.model.LoanDto(
                id = loan.id,
                amount = loan.amount,
                interestRate = loan.interestRate,
                tenure = loan.tenure,
                status = loan.status.name,
                disbursementDate = loan.disbursementDate,
                dueDate = loan.dueDate,
                remainingAmount = loan.remainingAmount
            )
            loanRemoteDataSource.createLoan(loanDto).toDomain()
        }
    }
}

