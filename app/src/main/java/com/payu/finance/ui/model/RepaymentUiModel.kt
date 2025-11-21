package com.payu.finance.ui.model

import com.payu.finance.domain.model.Repayment

/**
 * UI Model for Repayment
 * Contains presentation-specific data and formatting
 */
data class RepaymentUiModel(
    val id: String,
    val loanId: String,
    val amount: String, // Formatted amount
    val dueDate: String, // Formatted date
    val paidDate: String?,
    val status: RepaymentStatusUi,
    val installmentNumber: String // Formatted installment number
)

enum class RepaymentStatusUi {
    PENDING,
    PAID,
    OVERDUE,
    PARTIALLY_PAID
}

/**
 * Extension function to convert Domain model to UI model
 */
fun Repayment.toUiModel(): RepaymentUiModel {
    return RepaymentUiModel(
        id = id,
        loanId = loanId,
        amount = formatCurrency(amount),
        dueDate = formatDate(dueDate),
        paidDate = paidDate?.let { formatDate(it) },
        status = status.toUiStatus(),
        installmentNumber = "#$installmentNumber"
    )
}

private fun com.payu.finance.domain.model.RepaymentStatus.toUiStatus(): RepaymentStatusUi {
    return when (this) {
        com.payu.finance.domain.model.RepaymentStatus.PENDING -> RepaymentStatusUi.PENDING
        com.payu.finance.domain.model.RepaymentStatus.PAID -> RepaymentStatusUi.PAID
        com.payu.finance.domain.model.RepaymentStatus.OVERDUE -> RepaymentStatusUi.OVERDUE
        com.payu.finance.domain.model.RepaymentStatus.PARTIALLY_PAID -> RepaymentStatusUi.PARTIALLY_PAID
    }
}

private fun formatCurrency(amount: Double): String {
    return "₹${String.format("%.2f", amount)}"
}

private fun formatDate(dateString: String): String {
    // TODO: Implement proper date formatting
    return dateString
}

