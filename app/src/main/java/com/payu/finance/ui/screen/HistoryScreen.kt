package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onNavigateToLoanDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val historyResource by viewModel.historyResource.collectAsState()

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(paddingValues)
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
                    val historyItems = resource.data ?: emptyList()
                    if (historyItems.isEmpty()) {
                        EmptyHistoryView()
                    } else {
                        HistoryContent(
                            historyItems = historyItems,
                            onItemClick = { loanId ->
                                onNavigateToLoanDetail(loanId)
                            }
                        )
                    }
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
    historyItems: List<HistoryItem>,
    onItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = Spacing40,
            vertical = Spacing30
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing20)
    ) {
        item {
            ElevateText(
                markup = "Transaction History",
                style = LpTypography.TitleSection,
                color = PayUFinanceColors.ContentPrimary,
                modifier = Modifier.padding(bottom = Spacing30)
            )
        }

        items(
            items = historyItems,
            key = { it.id }
        ) { item ->
            HistoryItemCard(
                historyItem = item,
                onClick = { onItemClick(item.loanId) }
            )
        }
    }
}

/**
 * History Item Card
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

        HistoryContent(historyItems = mockHistoryItems)
    }
}

