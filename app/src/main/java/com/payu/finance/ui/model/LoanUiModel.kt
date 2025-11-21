package com.payu.finance.ui.model

import com.payu.finance.domain.model.Loan

/**
 * UI Model for Loan
 * Contains presentation-specific data and formatting
 */
data class LoanUiModel(
    val id: String,
    val amount: String, // Formatted amount
    val interestRate: String, // Formatted percentage
    val tenure: String, // Formatted tenure
    val status: LoanStatusUi,
    val disbursementDate: String, // Formatted date
    val dueDate: String?,
    val remainingAmount: String // Formatted amount
)

enum class LoanStatusUi {
    ACTIVE,
    COMPLETED,
    PENDING,
    OVERDUE
}

/**
 * Extension function to convert Domain model to UI model
 */
fun Loan.toUiModel(): LoanUiModel {
    return LoanUiModel(
        id = id,
        amount = formatCurrency(amount),
        interestRate = "${interestRate}%",
        tenure = "$tenure months",
        status = status.toUiStatus(),
        disbursementDate = formatDate(disbursementDate),
        dueDate = dueDate?.let { formatDate(it) },
        remainingAmount = formatCurrency(remainingAmount)
    )
}

private fun com.payu.finance.domain.model.LoanStatus.toUiStatus(): LoanStatusUi {
    return when (this) {
        com.payu.finance.domain.model.LoanStatus.ACTIVE -> LoanStatusUi.ACTIVE
        com.payu.finance.domain.model.LoanStatus.COMPLETED -> LoanStatusUi.COMPLETED
        com.payu.finance.domain.model.LoanStatus.PENDING -> LoanStatusUi.PENDING
        com.payu.finance.domain.model.LoanStatus.OVERDUE -> LoanStatusUi.OVERDUE
    }
}

private fun formatCurrency(amount: Double): String {
    return "₹${String.format("%.2f", amount)}"
}

private fun formatDate(dateString: String): String {
    // TODO: Implement proper date formatting
    return dateString
}

