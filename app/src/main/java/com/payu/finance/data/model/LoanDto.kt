package com.payu.finance.data.model

import com.payu.finance.domain.model.Loan
import com.payu.finance.domain.model.LoanStatus
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for Loan
 * This represents the API response structure
 */
data class LoanDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("interest_rate")
    val interestRate: Double,
    @SerializedName("tenure")
    val tenure: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("disbursement_date")
    val disbursementDate: String,
    @SerializedName("due_date")
    val dueDate: String?,
    @SerializedName("remaining_amount")
    val remainingAmount: Double
) {
    /**
     * Convert DTO to Domain model
     */
    fun toDomain(): Loan {
        return Loan(
            id = id,
            amount = amount,
            interestRate = interestRate,
            tenure = tenure,
            status = status.toLoanStatus(),
            disbursementDate = disbursementDate,
            dueDate = dueDate,
            remainingAmount = remainingAmount
        )
    }

    private fun String.toLoanStatus(): LoanStatus {
        return when (this.uppercase()) {
            "ACTIVE" -> LoanStatus.ACTIVE
            "COMPLETED" -> LoanStatus.COMPLETED
            "PENDING" -> LoanStatus.PENDING
            "OVERDUE" -> LoanStatus.OVERDUE
            else -> LoanStatus.PENDING
        }
    }
}

