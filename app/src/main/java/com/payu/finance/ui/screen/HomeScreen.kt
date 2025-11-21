package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.R
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
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToLoans: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToRepayment: (String) -> Unit = {},
    onNavigateToLoanDetail: (String) -> Unit = {},
    onNavigateToEmiSchedule: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val homeResource by viewModel.homeResource.collectAsState()

    when (val resource = homeResource) {
        is Resource.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Resource.Error -> {
            ErrorView(
                message = resource.message ?: "An error occurred",
                onRetry = { viewModel.handleEvent(HomeEvent.Refresh) },
                modifier = modifier
            )
        }
        is Resource.Success -> {
            val homeState = resource.data
            if (homeState != null) {
                HomeContent(
                    homeState = homeState,
                    onPayRepayment = { loanId ->
                        // Use loanId for navigation (repayment route expects loanId)
                        viewModel.handleEvent(HomeEvent.PayRepayment(loanId))
                        onNavigateToRepayment(loanId)
                    },
                    onViewAllDue = {
                        // Navigate to all due repayments
                    },
                    onNavigateToLoanDetail = onNavigateToLoanDetail,
                    onNavigateToEmiSchedule = onNavigateToEmiSchedule,
                    modifier = modifier.fillMaxSize()
                )
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
    onPayDue: () -> Unit = {},
    onViewAllDue: () -> Unit = {},
    onNavigateToLoanDetail: (String) -> Unit = {},
    onNavigateToEmiSchedule: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PayUFinanceColors.BackgroundPrimary),
        contentPadding = PaddingValues(bottom = 16.dp) // Extra padding to ensure last item is fully visible
    ) {
        // Background section with gradient image and cards - extends fully edge-to-edge behind status bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    // Extends fully behind status bar - no padding on the Box itself
            ) {
                // Background gradient image - extends fully edge-to-edge
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.ic_gradient),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
                
                // Cards container with proper padding (including status bar padding)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding() // Add status bar padding for content
                        .padding(
                            horizontal = Spacing40,
                            vertical = Spacing40
                        )
                ) {

                    ElevateText(
                        markup = homeState.userName,
                        style = LpTypography.TitleHeader,
                        color = ContentInversePrimary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    Spacer(modifier = Modifier.height(Spacing20))
                    ElevateText(
                        markup = homeState.subTitle,
                        style = LpTypography.BodyNormal,
                        color = ContentInversePrimary,
                        modifier = Modifier.padding(bottom = Spacing40)
                    )

                    // Due Card (shown first if there are overdue payments)
                    homeState.dueCard?.let { dueCard ->
                        // Get loanId from nextRepayment or use default
                        val loanId = homeState.nextRepayment?.loanId ?: "loan_1"
                        DueCard(
                            dueCard = dueCard,
                            onPayClick = onPayDue,
                            onCardClick = {
                                // Navigate to EMI Schedule when card is clicked
                                onNavigateToEmiSchedule(loanId)
                            },
                            onInfoClick = {
                                // Show info dialog or bottom sheet
                                // TODO: Implement info action for due card
                            },
                            modifier = Modifier.padding(bottom = Spacing20)
                        )
                    }

                    // EMI Progress Card
                    homeState.emiProgress?.let { emiProgress ->
                        Spacer(Modifier.size(Spacing40))
                        EmiProgressCard(
                            emiProgress = emiProgress,
                            modifier = Modifier.padding(bottom = Spacing20)
                        )
                    }
                }
            }
        }
        // White background section for remaining content
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PayUFinanceColors.BackgroundPrimary)
            ) {
                Column {
                    // Next Repayment Section Header
                    homeState.nextRepayment?.let {
                        SectionHeader(
                            title = "Next Repayment",
                            modifier = Modifier.padding(
                                horizontal = Spacing40,
                                vertical = Spacing20
                            )
                        )
                    }
                    
                    // Next Repayment Card
                    homeState.nextRepayment?.let { nextRepayment ->
                        NextRepaymentCard(
                            nextRepayment = nextRepayment,
                            onPayClick = { onPayRepayment(nextRepayment.loanId) },
                            onCardClick = {
                                // Navigate to EMI Schedule when card is clicked
                                onNavigateToEmiSchedule(nextRepayment.loanId)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = Spacing40,
                                ).padding(bottom = Spacing20)
                        )
                    }

                    // All EMIs Section Header
                    if (homeState.allEmis.isNotEmpty()) {
                        SectionHeader(
                            title = "All EMIs",
                            modifier = Modifier.padding(
                                horizontal = Spacing40,
                                vertical = Spacing20
                            )
                        )

                        // All EMIs List - Grouped card design
                        EmiListGroupedCard(
                            emis = homeState.allEmis,
                            onItemClick = { emiItem ->
                                // Navigate to loan detail screen when EMI card is clicked
                                onNavigateToLoanDetail(emiItem.loanId)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Spacing40)
                        )
                    } else {
                        EmptyState(
                            message = "No EMIs found",
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }
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
        color = PayUFinanceColors.ContentSecondary,
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
 * Shared component for error display across screens
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
            onNavigateToLoanDetail = {},
            onNavigateToEmiSchedule = {}
        )
    }
}

