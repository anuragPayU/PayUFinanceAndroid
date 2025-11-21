package com.payu.finance.ui.model

/**
 * UI Model for Home Screen
 */
data class HomeUiState(
    val userName: String = "Hi, Rahul",
    val subTitle: String = "Track all your loans",
    val emiProgress: EmiProgressCard? = null,
    val nextRepayment: NextRepaymentCard? = null,
    val dueCard: DueCard? = null,
    val allEmis: List<EmiItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * EMI Progress Card Data
 */
data class EmiProgressCard(
    val totalAmount: String,
    val paidAmount: String,
    val remainingAmount: String,
    val progressPercentage: Float, // 0.0 to 1.0
    val totalInstallments: Int,
    val paidInstallments: Int,
    val remainingInstallments: Int
)

/**
 * Next Repayment Card Data
 */
data class NextRepaymentCard(
    val amount: String,
    val dueDate: String,
    val daysRemaining: Int? = null, // null if overdue
    val isOverdue: Boolean = false,
    val showRepaymentCta: Boolean = true, // Some states don't show CTA
    val loanId: String,
    val repaymentId: String
)

/**
 * Due Card Data (shown when there are overdue payments)
 */
data class DueCard(
    val totalDueAmount: String,
    val overdueCount: Int,
    val nextDueDate: String?,
    val isUrgent: Boolean = false
)

/**
 * EMI Item for All EMIs Section
 */
data class EmiItem(
    val id: String,
    val installmentNumber: String,
    val amount: String,
    val dueDate: String,
    val status: EmiStatus,
    val loanId: String
)

enum class EmiStatus {
    PAID,
    PENDING,
    OVERDUE,
    PARTIALLY_PAID
}

