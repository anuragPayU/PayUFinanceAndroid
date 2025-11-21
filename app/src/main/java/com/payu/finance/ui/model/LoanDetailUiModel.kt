package com.payu.finance.ui.model

/**
 * UI Model for Loan Detail Screen
 * Based on API response structure with sections
 */
data class LoanDetailUiState(
    val sections: List<LoanDetailSectionUiItem>
)

/**
 * Section Item for Loan Detail UI
 */
sealed class LoanDetailSectionUiItem {
    /**
     * Detail Card Section - Shows loan amount and status
     */
    data class DetailCard(
        val title: String,
        val subtitle: String?,
        val statusLabel: String?,
        val statusColor: String?
    ) : LoanDetailSectionUiItem()

    /**
     * EMI Detail Section - Shows EMI information
     */
    data class EmiDetail(
        val title: String,
        val header: EmiDetailHeader?,
        val rows: List<EmiDetailRow>,
        val primaryAction: ActionItem?
    ) : LoanDetailSectionUiItem()

    /**
     * Auto Pay Status Section
     */
    data class AutoPayStatus(
        val title: String,
        val statusCard: AutoPayStatusCard
    ) : LoanDetailSectionUiItem()

    /**
     * Foreclosure Card Section
     */
    data class ForeclosureCard(
        val title: String,
        val card: ForeclosureCardItem
    ) : LoanDetailSectionUiItem()

    /**
     * Row List Card Section - For actions like document downloads
     */
    data class RowListCard(
        val title: String,
        val items: List<ActionableCardItem>
    ) : LoanDetailSectionUiItem()
}

/**
 * EMI Detail Header
 */
data class EmiDetailHeader(
    val title: String,
    val subtitle: String?,
    val percentage: String?
)

/**
 * EMI Detail Row
 */
data class EmiDetailRow(
    val title: String,
    val subtitle: String
)

/**
 * Auto Pay Status Card
 */
data class AutoPayStatusCard(
    val title: String,
    val subtitle: String?
)

/**
 * Foreclosure Card Item
 */
data class ForeclosureCardItem(
    val title: String,
    val subtitle: String,
    val description: String?,
    val action: ActionItem?
)

/**
 * Actionable Card Item
 */
data class ActionableCardItem(
    val title: String,
    val action: ActionItem?
)

/**
 * Action Item
 */
data class ActionItem(
    val text: String?,
    val type: String,
    val url: String?
)

enum class LoanStatus {
    ACTIVE,
    COMPLETED,
    PENDING,
    OVERDUE
}

