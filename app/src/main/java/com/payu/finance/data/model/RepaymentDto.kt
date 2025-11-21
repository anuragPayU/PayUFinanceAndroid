package com.payu.finance.data.model

import com.payu.finance.domain.model.Repayment
import com.payu.finance.domain.model.RepaymentStatus
import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) for Repayment
 * This represents the API response structure
 */
data class RepaymentDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("loan_id")
    val loanId: String,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("due_date")
    val dueDate: String,
    @SerializedName("paid_date")
    val paidDate: String?,
    @SerializedName("status")
    val status: String,
    @SerializedName("installment_number")
    val installmentNumber: Int
) {
    /**
     * Convert DTO to Domain model
     */
    fun toDomain(): Repayment {
        return Repayment(
            id = id,
            loanId = loanId,
            amount = amount,
            dueDate = dueDate,
            paidDate = paidDate,
            status = status.toRepaymentStatus(),
            installmentNumber = installmentNumber
        )
    }

    private fun String.toRepaymentStatus(): RepaymentStatus {
        return when (this.uppercase()) {
            "PENDING" -> RepaymentStatus.PENDING
            "PAID" -> RepaymentStatus.PAID
            "OVERDUE" -> RepaymentStatus.OVERDUE
            "PARTIALLY_PAID" -> RepaymentStatus.PARTIALLY_PAID
            else -> RepaymentStatus.PENDING
        }
    }
}

