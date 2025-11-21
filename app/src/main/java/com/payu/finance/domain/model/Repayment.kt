package com.payu.finance.domain.model

/**
 * Domain model for Repayment
 * This is the clean domain model used across the app
 */
data class Repayment(
    val id: String,
    val loanId: String,
    val amount: Double,
    val dueDate: String,
    val paidDate: String?,
    val status: RepaymentStatus,
    val installmentNumber: Int
)

enum class RepaymentStatus {
    PENDING,
    PAID,
    OVERDUE,
    PARTIALLY_PAID
}

