package com.payu.finance.data.api

import com.payu.finance.data.model.LoanDto
import com.payu.finance.data.model.LoanDetailScreenDto
import retrofit2.Response
import retrofit2.http.*

/**
 * API service interface for Loan endpoints
 * Retrofit will generate the implementation
 */
interface LoanApiService {
    @GET("loans")
    suspend fun getLoans(): Response<List<LoanDto>>

    @GET("loans/{id}")
    suspend fun getLoanById(@Path("id") loanId: String): Response<LoanDto>

    @GET("loans/{id}/detail")
    suspend fun getLoanDetailScreenContent(@Path("id") loanId: String): Response<LoanDetailScreenDto>

    @POST("loans")
    suspend fun createLoan(@Body loan: LoanDto): Response<LoanDto>
}

