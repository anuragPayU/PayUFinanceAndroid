package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.ButtonDefaults
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.common.Resource
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.components.*
import com.payu.finance.ui.model.*
import com.payu.finance.navigation.MainRoutes
import com.payu.finance.ui.viewmodel.HomeEvent
import com.payu.finance.ui.viewmodel.HomeViewModel

/**
 * Main Home Screen (without bottom navigation - handled by MainScreen wrapper)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToLoans: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRepayment: (String) -> Unit = {},
    onNavigateToLoanDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val homeResource by viewModel.homeResource.collectAsState()

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(paddingValues)
        ) {
            when (val resource = homeResource) {
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
                        onRetry = { viewModel.handleEvent(HomeEvent.Refresh) }
                    )
                }
                is Resource.Success -> {
                    val homeState = resource.data
                    if (homeState != null) {
                        HomeContent(
                            homeState = homeState,
                            onPayRepayment = { repaymentId ->
                                viewModel.handleEvent(HomeEvent.PayRepayment(repaymentId))
                                onNavigateToRepayment(repaymentId)
                            },
                            onViewAllDue = {
                                // Navigate to all due repayments
                            },
                            onNavigateToLoanDetail = onNavigateToLoanDetail
                        )
                    }
                }
            }
        }
    }
}

/**
 * Home Content with all cards
 */
@Composable
fun HomeContent(
    homeState: HomeUiState,
    onPayRepayment: (String) -> Unit = {},
    onViewAllDue: () -> Unit = {},
    onNavigateToLoanDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PayUFinanceColors.BackgroundPrimary),
        verticalArrangement = Arrangement.spacedBy(Spacing20),
        contentPadding = PaddingValues(
            horizontal = Spacing40,
            vertical = Spacing30
        )
    ) {
        // Due Card (shown first if there are overdue payments)
        homeState.dueCard?.let { dueCard ->
            item {
                DueCard(
                    dueCard = dueCard,
                    onViewAllClick = onViewAllDue
                )
            }
        }

        // EMI Progress Card
        homeState.emiProgress?.let { emiProgress ->
            item {
                EmiProgressCard(emiProgress = emiProgress)
            }
        }

        // Next Repayment Card
        homeState.nextRepayment?.let { nextRepayment ->
            item {
                NextRepaymentCard(
                    nextRepayment = nextRepayment,
                    onPayClick = { onPayRepayment(nextRepayment.repaymentId) }
                )
            }
        }

        // All EMIs Section Header
        if (homeState.allEmis.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "All EMIs",
                    modifier = Modifier.padding(vertical = Spacing20)
                )
            }

            // All EMIs List
            items(homeState.allEmis) { emiItem ->
                EmiItemCard(
                    emiItem = emiItem,
                    onClick = {
                        // Navigate to loan detail screen when EMI card is clicked
                        onNavigateToLoanDetail(emiItem.loanId)
                    }
                )
            }
        } else {
            item {
                EmptyState(
                    message = "No EMIs found",
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}

/**
 * Section Header Component
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    ElevateText(
        markup = title,
        style = LpTypography.TitleSection,
        color = PayUFinanceColors.ContentPrimary,
        modifier = modifier
    )
}

/**
 * Empty State Component
 */
@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ElevateText(
            markup = message,
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentSecondary,
            modifier = Modifier.padding(Spacing40)
        )
    }
}

/**
 * Error View Component
 */
@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing40),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevateText(
            markup = message,
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.Error,
            modifier = Modifier.padding(bottom = Spacing30)
        )
        LPButton(
            text = "Retry",
            state = ButtonState.Enabled,
            backgroundColor = PayUFinanceColors.Primary,
            pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
            disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
            contentColor = PayUFinanceColors.BackgroundPrimary,
            pressedContentColor = PayUFinanceColors.BackgroundPrimary,
            disabledContentColor = PayUFinanceColors.ContentTertiary,
            circularProgressColor = PayUFinanceColors.BackgroundPrimary,
            buttonElevation = ButtonDefaults.elevation(),
            onClick = onRetry
        )
    }
}


// Preview Composable
@Preview(showBackground = true, name = "Home Screen Preview")
@Composable
private fun HomeScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        val mockHomeState = HomeUiState(
            emiProgress = EmiProgressCard(
                totalAmount = "₹1,00,000",
                paidAmount = "₹40,000",
                remainingAmount = "₹60,000",
                progressPercentage = 0.4f,
                totalInstallments = 12,
                paidInstallments = 5,
                remainingInstallments = 7
            ),
            nextRepayment = NextRepaymentCard(
                amount = "₹8,333",
                dueDate = "15 Jan 2024",
                daysRemaining = 5,
                isOverdue = false,
                showRepaymentCta = true,
                loanId = "loan_1",
                repaymentId = "repayment_1"
            ),
            dueCard = DueCard(
                totalDueAmount = "₹16,666",
                overdueCount = 2,
                nextDueDate = "10 Jan 2024",
                isUrgent = false
            ),
            allEmis = listOf(
                EmiItem(
                    id = "emi_1",
                    installmentNumber = "#1",
                    amount = "₹8,333",
                    dueDate = "15 Dec 2023",
                    status = EmiStatus.PAID,
                    loanId = "loan_1"
                ),
                EmiItem(
                    id = "emi_2",
                    installmentNumber = "#2",
                    amount = "₹8,333",
                    dueDate = "15 Jan 2024",
                    status = EmiStatus.PENDING,
                    loanId = "loan_1"
                ),
                EmiItem(
                    id = "emi_3",
                    installmentNumber = "#3",
                    amount = "₹8,333",
                    dueDate = "15 Feb 2024",
                    status = EmiStatus.PENDING,
                    loanId = "loan_1"
                )
            )
        )
        
        HomeContent(
            homeState = mockHomeState,
            onPayRepayment = {},
            onViewAllDue = {},
            onNavigateToLoanDetail = {}
        )
    }
}

