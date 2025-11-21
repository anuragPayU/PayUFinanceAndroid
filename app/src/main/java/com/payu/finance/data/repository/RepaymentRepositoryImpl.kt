package com.payu.finance.data.repository

import com.payu.finance.common.Result
import com.payu.finance.data.datasource.RepaymentRemoteDataSource
import com.payu.finance.domain.model.Repayment
import com.payu.finance.domain.repository.RepaymentRepository

/**
 * Implementation of RepaymentRepository
 * Maps data layer to domain layer
 */
class RepaymentRepositoryImpl(
    private val repaymentRemoteDataSource: RepaymentRemoteDataSource
) : RepaymentRepository {

    override suspend fun getRepayments(loanId: String?): Result<List<Repayment>> {
        return safeApiCall {
            repaymentRemoteDataSource.getRepayments(loanId).map { it.toDomain() }
        }
    }

    override suspend fun getRepaymentById(repaymentId: String): Result<Repayment> {
        return safeApiCall {
            repaymentRemoteDataSource.getRepaymentById(repaymentId).toDomain()
        }
    }

    override suspend fun createRepayment(repayment: Repayment): Result<Repayment> {
        return safeApiCall {
            // Convert domain model to DTO
            val repaymentDto = com.payu.finance.data.model.RepaymentDto(
                id = repayment.id,
                loanId = repayment.loanId,
                amount = repayment.amount,
                dueDate = repayment.dueDate,
                paidDate = repayment.paidDate,
                status = repayment.status.name,
                installmentNumber = repayment.installmentNumber
            )
            repaymentRemoteDataSource.createRepayment(repaymentDto).toDomain()
        }
    }

    override suspend fun updateRepaymentStatus(
        repaymentId: String,
        status: String
    ): Result<Repayment> {
        return safeApiCall {
            repaymentRemoteDataSource.updateRepaymentStatus(repaymentId, status).toDomain()
        }
    }
}

