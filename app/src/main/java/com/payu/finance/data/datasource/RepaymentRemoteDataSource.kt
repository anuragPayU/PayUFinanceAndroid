package com.payu.finance.data.datasource

import com.payu.finance.data.api.RepaymentApiService
import com.payu.finance.data.model.RepaymentDto

/**
 * Remote data source for Repayment
 * Handles API calls and error handling
 */
class RepaymentRemoteDataSource(
    private val repaymentApiService: RepaymentApiService
) {
    suspend fun getRepayments(loanId: String? = null): List<RepaymentDto> {
        val response = repaymentApiService.getRepayments(loanId)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch repayments: ${response.message()}")
        }
    }

    suspend fun getRepaymentById(repaymentId: String): RepaymentDto {
        val response = repaymentApiService.getRepaymentById(repaymentId)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch repayment: ${response.message()}")
        }
    }

    suspend fun createRepayment(repayment: RepaymentDto): RepaymentDto {
        val response = repaymentApiService.createRepayment(repayment)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to create repayment: ${response.message()}")
        }
    }

    suspend fun updateRepaymentStatus(repaymentId: String, status: String): RepaymentDto {
        val response = repaymentApiService.updateRepaymentStatus(
            repaymentId,
            mapOf("status" to status)
        )
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to update repayment status: ${response.message()}")
        }
    }
}

