package com.payu.finance.domain.model

/**
 * Domain model for Loan
 * This is the clean domain model used across the app
 */
data class Loan(
    val id: String,
    val amount: Double,
    val interestRate: Double,
    val tenure: Int, // in months
    val status: LoanStatus,
    val disbursementDate: String,
    val dueDate: String?,
    val remainingAmount: Double
)

enum class LoanStatus {
    ACTIVE,
    COMPLETED,
    PENDING,
    OVERDUE
}

