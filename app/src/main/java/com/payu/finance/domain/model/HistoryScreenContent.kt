package com.payu.finance.domain.model

/**
 * Domain model for History Screen Content
 */
data class HistoryScreenContent(
    val title: String,
    val subtitle: String,
    val actions: HistoryActions?,
    val sections: List<HistorySectionItem>
)

/**
 * History Actions
 */
data class HistoryActions(
    val primary: ActionItem?
)

/**
 * History Section Item
 */
data class HistorySectionItem(
    val type: String,
    val className: String?,
    val components: List<HistoryComponentItem>?
)

/**
 * History Component Item
 */
data class HistoryComponentItem(
    val title: String?,
    val subtitle: String?,
    val description: String?,
    val meta: HistoryMeta?,
    val assets: HistoryAssets?
)

/**
 * History Meta
 */
data class HistoryMeta(
    val label: String?,
    val color: String?
)

/**
 * History Assets
 */
data class HistoryAssets(
    val leadingIcon: String?
)

