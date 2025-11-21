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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.ui.components.StatusChip
import com.payu.finance.ui.model.EmiStatus
import com.payu.finance.ui.theme.PayUFinanceColors

/**
 * EMI Schedule Screen - Shows detailed schedule of all EMIs for a loan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmiScheduleScreen(
    loanId: String,
    onBackClick: () -> Unit,
    onPayEmiClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Dummy data for preview
    val emiScheduleState = EmiScheduleUiState(
        loanId = loanId,
        loanAmount = "₹4,00,000",
        loanTitle = "Personal Loan",
        totalEmis = 12,
        paidEmis = 5,
        remainingEmis = 7,
        totalPaid = "₹1,66,650",
        totalRemaining = "₹2,33,350",
        emiAmount = "₹33,330",
        emis = listOf(
            EmiScheduleItem(
                id = "emi_1",
                installmentNumber = 1,
                amount = "₹33,330",
                dueDate = "15 Dec 2023",
                paidDate = "14 Dec 2023",
                status = EmiStatus.PAID,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_2",
                installmentNumber = 2,
                amount = "₹33,330",
                dueDate = "15 Jan 2024",
                paidDate = "14 Jan 2024",
                status = EmiStatus.PAID,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_3",
                installmentNumber = 3,
                amount = "₹33,330",
                dueDate = "15 Feb 2024",
                paidDate = "14 Feb 2024",
                status = EmiStatus.PAID,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_4",
                installmentNumber = 4,
                amount = "₹33,330",
                dueDate = "15 Mar 2024",
                paidDate = "14 Mar 2024",
                status = EmiStatus.PAID,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_5",
                installmentNumber = 5,
                amount = "₹33,330",
                dueDate = "15 Apr 2024",
                paidDate = "14 Apr 2024",
                status = EmiStatus.PAID,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_6",
                installmentNumber = 6,
                amount = "₹33,330",
                dueDate = "15 May 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_7",
                installmentNumber = 7,
                amount = "₹33,330",
                dueDate = "15 Jun 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_8",
                installmentNumber = 8,
                amount = "₹33,330",
                dueDate = "15 Jul 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_9",
                installmentNumber = 9,
                amount = "₹33,330",
                dueDate = "15 Aug 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_10",
                installmentNumber = 10,
                amount = "₹33,330",
                dueDate = "15 Sep 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_11",
                installmentNumber = 11,
                amount = "₹33,330",
                dueDate = "15 Oct 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            ),
            EmiScheduleItem(
                id = "emi_12",
                installmentNumber = 12,
                amount = "₹33,330",
                dueDate = "15 Nov 2024",
                paidDate = null,
                status = EmiStatus.PENDING,
                principal = "₹30,000",
                interest = "₹3,330"
            )
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    ElevateText(
                        markup = "EMI Schedule",
                        style = LpTypography.TitleSection,
                        color = PayUFinanceColors.ContentPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = PayUFinanceColors.ContentPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PayUFinanceColors.BackgroundPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .statusBarsPadding()
                .padding(paddingValues)
        ) {
            EmiScheduleContent(
                emiScheduleState = emiScheduleState,
                onPayEmiClick = onPayEmiClick
            )
        }
    }
}

/**
 * EMI Schedule Content
 */
@Composable
fun EmiScheduleContent(
    emiScheduleState: EmiScheduleUiState,
    onPayEmiClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = Spacing40,
            vertical = Spacing20
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing30)
    ) {
        // Summary Card
        item {
            EmiScheduleSummaryCard(
                loanAmount = emiScheduleState.loanAmount,
                loanTitle = emiScheduleState.loanTitle,
                totalEmis = emiScheduleState.totalEmis,
                paidEmis = emiScheduleState.paidEmis,
                remainingEmis = emiScheduleState.remainingEmis,
                totalPaid = emiScheduleState.totalPaid,
                totalRemaining = emiScheduleState.totalRemaining,
                emiAmount = emiScheduleState.emiAmount
            )
        }

        // Section Header
        item {
            SectionHeader(
                title = "Installment Schedule",
                modifier = Modifier.padding(top = Spacing10)
            )
        }

        // EMI List
        items(
            items = emiScheduleState.emis,
            key = { it.id }
        ) { emiItem ->
            EmiScheduleItemCard(
                emiItem = emiItem,
                onPayClick = { onPayEmiClick(emiItem.id) }
            )
        }
    }
}

/**
 * EMI Schedule Summary Card
 */
@Composable
fun EmiScheduleSummaryCard(
    loanAmount: String,
    loanTitle: String,
    totalEmis: Int,
    paidEmis: Int,
    remainingEmis: Int,
    totalPaid: String,
    totalRemaining: String,
    emiAmount: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(Spacing40)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing20)
            ) {
                // Loan Title and Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ElevateText(
                            markup = loanTitle,
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = loanAmount,
                            style = LpTypography.TitleSection,
                            color = PayUFinanceColors.ContentPrimary
                        )
                    }
                }

                Divider(
                    color = PayUFinanceColors.BorderPrimary,
                    thickness = 1.dp
                )

                // EMI Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevateText(
                        markup = "EMI Amount",
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentSecondary
                    )
                    ElevateText(
                        markup = emiAmount,
                        style = LpTypography.TitlePrimary,
                        color = PayUFinanceColors.ContentPrimary
                    )
                }

                Divider(
                    color = PayUFinanceColors.BorderPrimary,
                    thickness = 1.dp
                )

                // Installment Progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ElevateText(
                        markup = "Installments",
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentSecondary
                    )
                    ElevateText(
                        markup = "$paidEmis/$totalEmis Paid",
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentPrimary
                    )
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = paidEmis.toFloat() / totalEmis.toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PayUFinanceColors.ProgressBarFill,
                    trackColor = PayUFinanceColors.ProgressBarTrack
                )

                Divider(
                    color = PayUFinanceColors.BorderPrimary,
                    thickness = 1.dp
                )

                // Total Paid and Remaining
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        ElevateText(
                            markup = "Total Paid",
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = totalPaid,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.Success
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        ElevateText(
                            markup = "Remaining",
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = totalRemaining,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentPrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * EMI Schedule Item Card
 */
@Composable
fun EmiScheduleItemCard(
    emiItem: EmiScheduleItem,
    onPayClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(Spacing40)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing20)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing20)
                    ) {
                        // Status Icon
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(Spacing20))
                                .background(
                                    when (emiItem.status) {
                                        EmiStatus.PAID -> PayUFinanceColors.SuccessBackground
                                        EmiStatus.OVERDUE -> PayUFinanceColors.ErrorBackground
                                        EmiStatus.PENDING -> PayUFinanceColors.WarningBackground
                                        EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.PrimaryLight
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (emiItem.status) {
                                    EmiStatus.PAID -> Icons.Default.CheckCircle
                                    EmiStatus.OVERDUE -> Icons.Default.Info
                                    EmiStatus.PENDING -> Icons.Default.Info
                                    EmiStatus.PARTIALLY_PAID -> Icons.Default.Info
                                },
                                contentDescription = null,
                                tint = when (emiItem.status) {
                                    EmiStatus.PAID -> PayUFinanceColors.Success
                                    EmiStatus.OVERDUE -> PayUFinanceColors.Error
                                    EmiStatus.PENDING -> PayUFinanceColors.Warning
                                    EmiStatus.PARTIALLY_PAID -> PayUFinanceColors.Primary
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            ElevateText(
                                markup = "Installment #${emiItem.installmentNumber}",
                                style = LpTypography.TitlePrimary,
                                color = PayUFinanceColors.ContentPrimary,
                                modifier = Modifier.padding(bottom = Spacing10)
                            )
                            ElevateText(
                                markup = emiItem.amount,
                                style = LpTypography.TitleSection,
                                color = PayUFinanceColors.ContentPrimary
                            )
                        }
                    }

                    StatusChip(status = emiItem.status)
                }

                Divider(
                    color = PayUFinanceColors.BorderPrimary,
                    thickness = 1.dp
                )

                // Details Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        ElevateText(
                            markup = "Due Date",
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = emiItem.dueDate,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentPrimary
                        )
                    }
                    emiItem.paidDate?.let { paidDate ->
                        Column(horizontalAlignment = Alignment.End) {
                            ElevateText(
                                markup = "Paid Date",
                                style = LpTypography.BodySmall,
                                color = PayUFinanceColors.ContentSecondary,
                                modifier = Modifier.padding(bottom = Spacing10)
                            )
                            ElevateText(
                                markup = paidDate,
                                style = LpTypography.BodyNormal,
                                color = PayUFinanceColors.Success
                            )
                        }
                    }
                }

                // Principal and Interest Breakdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        ElevateText(
                            markup = "Principal",
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = emiItem.principal,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentPrimary
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        ElevateText(
                            markup = "Interest",
                            style = LpTypography.BodySmall,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.padding(bottom = Spacing10)
                        )
                        ElevateText(
                            markup = emiItem.interest,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentPrimary
                        )
                    }
                }

                // Pay Button for Pending EMIs
                if (emiItem.status == EmiStatus.PENDING || emiItem.status == EmiStatus.OVERDUE) {
                    LPButton(
                        text = "Pay Now",
                        state = ButtonState.Enabled,
                        backgroundColor = PayUFinanceColors.Primary,
                        pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
                        disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                        contentColor = PayUFinanceColors.BackgroundPrimary,
                        pressedContentColor = PayUFinanceColors.BackgroundPrimary,
                        disabledContentColor = PayUFinanceColors.ContentTertiary,
                        circularProgressColor = PayUFinanceColors.BackgroundPrimary,
                        buttonElevation = ButtonDefaults.elevation(),
                        onClick = onPayClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

///**
// * Section Header Component
// */
//@Composable
//fun SectionHeader(
//    title: String,
//    modifier: Modifier = Modifier
//) {
//    ElevateText(
//        markup = title,
//        style = LpTypography.TitleSection,
//        color = PayUFinanceColors.ContentSecondary,
//        modifier = modifier
//    )
//}

/**
 * EMI Schedule UI State
 */
data class EmiScheduleUiState(
    val loanId: String,
    val loanAmount: String,
    val loanTitle: String,
    val totalEmis: Int,
    val paidEmis: Int,
    val remainingEmis: Int,
    val totalPaid: String,
    val totalRemaining: String,
    val emiAmount: String,
    val emis: List<EmiScheduleItem>
)

/**
 * EMI Schedule Item
 */
data class EmiScheduleItem(
    val id: String,
    val installmentNumber: Int,
    val amount: String,
    val dueDate: String,
    val paidDate: String?,
    val status: EmiStatus,
    val principal: String,
    val interest: String
)

// Preview
@Preview(showBackground = true, name = "EMI Schedule Screen Preview")
@Composable
fun EmiScheduleScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        EmiScheduleScreen(
            loanId = "loan_1",
            onBackClick = {},
            onPayEmiClick = {}
        )
    }
}

@Preview(showBackground = true, name = "EMI Schedule Content Preview")
@Composable
fun EmiScheduleContentPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        val mockEmiScheduleState = EmiScheduleUiState(
            loanId = "loan_1",
            loanAmount = "₹4,00,000",
            loanTitle = "Personal Loan",
            totalEmis = 12,
            paidEmis = 5,
            remainingEmis = 7,
            totalPaid = "₹1,66,650",
            totalRemaining = "₹2,33,350",
            emiAmount = "₹33,330",
            emis = listOf(
                EmiScheduleItem(
                    id = "emi_1",
                    installmentNumber = 1,
                    amount = "₹33,330",
                    dueDate = "15 Dec 2023",
                    paidDate = "14 Dec 2023",
                    status = EmiStatus.PAID,
                    principal = "₹30,000",
                    interest = "₹3,330"
                ),
                EmiScheduleItem(
                    id = "emi_2",
                    installmentNumber = 2,
                    amount = "₹33,330",
                    dueDate = "15 Jan 2024",
                    paidDate = "14 Jan 2024",
                    status = EmiStatus.PAID,
                    principal = "₹30,000",
                    interest = "₹3,330"
                ),
                EmiScheduleItem(
                    id = "emi_6",
                    installmentNumber = 6,
                    amount = "₹33,330",
                    dueDate = "15 May 2024",
                    paidDate = null,
                    status = EmiStatus.PENDING,
                    principal = "₹30,000",
                    interest = "₹3,330"
                ),
                EmiScheduleItem(
                    id = "emi_7",
                    installmentNumber = 7,
                    amount = "₹33,330",
                    dueDate = "15 Jun 2024",
                    paidDate = null,
                    status = EmiStatus.OVERDUE,
                    principal = "₹30,000",
                    interest = "₹3,330"
                )
            )
        )
        
        EmiScheduleContent(
            emiScheduleState = mockEmiScheduleState,
            onPayEmiClick = {}
        )
    }
}

