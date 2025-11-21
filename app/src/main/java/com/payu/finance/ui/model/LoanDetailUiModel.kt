package com.payu.finance.ui.model

/**
 * UI Model for Loan Detail Screen
 */
data class LoanDetailUiState(
    val loanId: String,
    val loanAmount: String,
    val interestRate: String,
    val tenure: Int, // in months
    val status: LoanStatus,
    val disbursementDate: String,
    val dueDate: String?,
    val remainingAmount: String,
    val paidAmount: String,
    val totalInstallments: Int,
    val paidInstallments: Int,
    val nextEmiDate: String?,
    val nextEmiAmount: String?,
    val emiBreakdown: List<EmiBreakdownItem> = emptyList(),
    val loanAgreementUrl: String? = null,
    val sanctionLetterUrl: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * EMI Breakdown Item
 */
data class EmiBreakdownItem(
    val installmentNumber: Int,
    val amount: String,
    val dueDate: String,
    val status: EmiStatus,
    val principalAmount: String,
    val interestAmount: String
)

enum class LoanStatus {
    ACTIVE,
    COMPLETED,
    PENDING,
    OVERDUE
}

