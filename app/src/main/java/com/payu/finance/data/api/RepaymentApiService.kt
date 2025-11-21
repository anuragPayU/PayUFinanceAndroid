package com.payu.finance.data.api

import com.payu.finance.data.model.RepaymentDto
import retrofit2.Response
import retrofit2.http.*

/**
 * API service interface for Repayment endpoints
 * Retrofit will generate the implementation
 */
interface RepaymentApiService {
    @GET("repayments")
    suspend fun getRepayments(@Query("loan_id") loanId: String? = null): Response<List<RepaymentDto>>

    @GET("repayments/{id}")
    suspend fun getRepaymentById(@Path("id") repaymentId: String): Response<RepaymentDto>

    @POST("repayments")
    suspend fun createRepayment(@Body repayment: RepaymentDto): Response<RepaymentDto>

    @PUT("repayments/{id}/status")
    suspend fun updateRepaymentStatus(
        @Path("id") repaymentId: String,
        @Body statusUpdate: Map<String, String>
    ): Response<RepaymentDto>
}

