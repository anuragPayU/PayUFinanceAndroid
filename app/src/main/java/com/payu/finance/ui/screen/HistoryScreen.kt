package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.common.Resource
import com.payu.finance.ui.components.StatusChip
import com.payu.finance.ui.model.EmiStatus
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.viewmodel.HistoryViewModel

/**
 * History Screen - Shows transaction/repayment history
 */
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateToLoanDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val historyResource by viewModel.historyResource.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PayUFinanceColors.BackgroundPrimary)
    ) {
        when (val resource = historyResource) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Error -> {
                ErrorView(
                    message = resource.message ?: "An error occurred",
                    onRetry = { viewModel.loadHistory() }
                )
            }
            is Resource.Success -> {
                val historyState = resource.data
                if (historyState != null) {
                    if (historyState.items.isEmpty()) {
                        EmptyHistoryView()
                    } else {
                        HistoryContent(
                            historyState = historyState,
                            onItemClick = { loanId ->
                                onNavigateToLoanDetail(loanId)
                            }
                        )
                    }
                } else {
                    EmptyHistoryView()
                }
            }
        }
    }
}

/**
 * History Content with list of transactions
 */
@Composable
fun HistoryContent(
    historyState: HistoryUiState,
    onItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
            .padding(16.dp), // Extra padding to ensure last item is fully visible above navigation bar,),
        contentPadding = PaddingValues(
            horizontal = Spacing40,
            vertical = Spacing30
        )
    ) {
        item {
            HistoryHeader(
                header = historyState.header,
                modifier = Modifier.padding(bottom = Spacing30)
            )
        }

        item {
            HistoryListGroupedCard(
                historyItems = historyState.items,
                onItemClick = { historyItem ->
                    onItemClick(historyItem.loanId)
                }
            )
        }
    }
}

/**
 * History Header Component
 */
@Composable
fun HistoryHeader(
    header: HistoryHeader,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ElevateText(
            markup = header.title,
            style = LpTypography.TitlePrimary,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing10)
        )
        if (header.subtitle.isNotEmpty()) {
            ElevateText(
                markup = header.subtitle,
                style = LpTypography.BodyNormal,
                color = PayUFinanceColors.ContentSecondary
            )
        }
    }
}

/**
 * Grouped History List Card
 * Single card container with borders and rounded corners containing all history items
 */
@Composable
fun HistoryListGroupedCard(
    historyItems: List<HistoryItem>,
    onItemClick: (HistoryItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (historyItems.isEmpty()) return
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.BackgroundPrimary
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            historyItems.forEachIndexed { index, historyItem ->
                HistoryItemContent(
                    historyItem = historyItem,
                    onClick = { onItemClick(historyItem) },
                    showDivider = index < historyItems.size - 1
                )
            }
        }
    }
}

/**
 * History Item Content (without card wrapper, used inside grouped card)
 */
@Composable
private fun HistoryItemContent(
    historyItem: HistoryItem,
    onClick: () -> Unit = {},
    showDivider: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Icon and Content
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Transaction Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(Spacing20))
                        .background(
                            when (historyItem.status) {
                                EmiStatus.PAID -> PayUFinanceColors.SuccessBackground
                                EmiStatus.OVERDUE -> PayUFinanceColors.ErrorBackground
                                EmiStatus.PENDING -> PayUFinanceColors.WarningBackground
                                EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.PrimaryLight
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (historyItem.status) {
                            EmiStatus.PAID -> Icons.Default.CheckCircle
                            EmiStatus.OVERDUE -> Icons.Default.Warning
                            EmiStatus.PENDING -> Icons.Default.List
                            EmiStatus.PARTIALLY_PAID -> Icons.Default.List
                        },
                        contentDescription = null,
                        tint = when (historyItem.status) {
                            EmiStatus.PAID -> PayUFinanceColors.Success
                            EmiStatus.OVERDUE -> PayUFinanceColors.Error
                            EmiStatus.PENDING -> PayUFinanceColors.Warning
                            EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.Primary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(Spacing20))
                
                // Content Column
                Column(modifier = Modifier.weight(1f)) {
                    ElevateText(
                        markup = historyItem.title,
                        style = LpTypography.TitleSecondary,
                        color = PayUFinanceColors.ContentPrimary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    ElevateText(
                        markup = historyItem.date,
                        style = LpTypography.BodySmall,
                        color = PayUFinanceColors.ContentSecondary,
                        modifier = Modifier.padding(bottom = if (historyItem.description.isNotEmpty()) Spacing10 else 0.dp)
                    )
                    if (historyItem.description.isNotEmpty()) {
                        ElevateText(
                            markup = historyItem.description,
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentTertiary
                        )
                    }
                }
            }
            
            // Right side: Amount and Status
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing10)
            ) {
                ElevateText(
                    markup = historyItem.amount,
                    style = LpTypography.TitlePrimary,
                    color = PayUFinanceColors.ContentPrimary
                )
                StatusChip(status = historyItem.status)
            }
        }
        
        // Divider between items (not after last item)
        if (showDivider) {
            HorizontalDivider(
                color = PayUFinanceColors.BorderPrimary,
                thickness = 1.dp
            )
        }
    }
}

/**
 * History Item Card (legacy - kept for backward compatibility if needed)
 */
@Composable
fun HistoryItemCard(
    historyItem: HistoryItem,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.BackgroundPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Icon and Content
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Transaction Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(Spacing20))
                        .background(
                            when (historyItem.status) {
                                EmiStatus.PAID -> PayUFinanceColors.SuccessBackground
                                EmiStatus.OVERDUE -> PayUFinanceColors.ErrorBackground
                                EmiStatus.PENDING -> PayUFinanceColors.WarningBackground
                                EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.PrimaryLight
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (historyItem.status) {
                            EmiStatus.PAID -> Icons.Default.CheckCircle
                            EmiStatus.OVERDUE -> Icons.Default.Warning
                            EmiStatus.PENDING -> Icons.Default.List
                            EmiStatus.PARTIALLY_PAID -> Icons.Default.List
                        },
                        contentDescription = null,
                        tint = when (historyItem.status) {
                            EmiStatus.PAID -> PayUFinanceColors.Success
                            EmiStatus.OVERDUE -> PayUFinanceColors.Error
                            EmiStatus.PENDING -> PayUFinanceColors.Warning
                            EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.Primary
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(Spacing20))
                
                // Content Column
                Column(modifier = Modifier.weight(1f)) {
                    ElevateText(
                        markup = historyItem.title,
                        style = LpTypography.TitleSecondary,
                        color = PayUFinanceColors.ContentPrimary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    ElevateText(
                        markup = historyItem.date,
                        style = LpTypography.BodySmall,
                        color = PayUFinanceColors.ContentSecondary,
                        modifier = Modifier.padding(bottom = if (historyItem.description.isNotEmpty()) Spacing10 else 0.dp)
                    )
                    if (historyItem.description.isNotEmpty()) {
                        ElevateText(
                            markup = historyItem.description,
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentTertiary
                        )
                    }
                }
            }
            
            // Right side: Amount and Status
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing10)
            ) {
                ElevateText(
                    markup = historyItem.amount,
                    style = LpTypography.TitlePrimary,
                    color = PayUFinanceColors.ContentPrimary
                )
                StatusChip(status = historyItem.status)
            }
        }
    }
}

/**
 * Empty History View
 */
@Composable
fun EmptyHistoryView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "No History",
            tint = PayUFinanceColors.ContentTertiary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(Spacing30))
        ElevateText(
            markup = "No Transaction History",
            style = LpTypography.TitleSecondary,
            color = PayUFinanceColors.ContentSecondary
        )
        Spacer(modifier = Modifier.height(Spacing10))
        ElevateText(
            markup = "Your transaction history will appear here",
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentTertiary
        )
    }
}

/**
 * History UI State
 */
data class HistoryUiState(
    val header: HistoryHeader,
    val items: List<HistoryItem>
)

/**
 * History Header Data Model
 */
data class HistoryHeader(
    val title: String,
    val subtitle: String
)

/**
 * History Item Data Model
 */
data class HistoryItem(
    val id: String,
    val title: String,
    val description: String,
    val amount: String,
    val date: String,
    val status: EmiStatus,
    val loanId: String
)

// Preview
@Preview(showBackground = true, name = "History Screen Preview")
@Composable
private fun HistoryScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        val mockHistoryItems = listOf(
            HistoryItem(
                id = "1",
                title = "EMI Payment",
                description = "Loan #LOAN123",
                amount = "₹8,333",
                date = "15 Jan 2024",
                status = EmiStatus.PAID,
                loanId = "loan_1"
            ),
            HistoryItem(
                id = "2",
                title = "EMI Payment",
                description = "Loan #LOAN123",
                amount = "₹8,333",
                date = "15 Dec 2023",
                status = EmiStatus.PAID,
                loanId = "loan_1"
            ),
            HistoryItem(
                id = "3",
                title = "EMI Payment",
                description = "Loan #LOAN456",
                amount = "₹10,000",
                date = "10 Dec 2023",
                status = EmiStatus.PAID,
                loanId = "loan_2"
            )
        )

        val mockHistoryState = HistoryUiState(
            header = HistoryHeader(
                title = "Transaction History",
                subtitle = "View all your past transactions"
            ),
            items = mockHistoryItems
        )
        HistoryContent(historyState = mockHistoryState)
    }
}

