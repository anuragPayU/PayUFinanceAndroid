package com.payu.finance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.ButtonDefaults
import com.lazypay.android.elevate.Elevate
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.ui.model.*
import com.payu.finance.ui.theme.PayUFinanceColors

/**
 * EMI Progress Card Component
 */
@Composable
fun EmiProgressCard(
    emiProgress: EmiProgressCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40)
        ) {
            ElevateText(
                markup = "EMI Progress",
                style = LpTypography.TitlePrimary,
                color = PayUFinanceColors.ContentPrimary,
                modifier = Modifier.padding(bottom = Spacing30)
            )
            
            // Progress Bar
            LinearProgressIndicator(
                progress = emiProgress.progressPercentage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PayUFinanceColors.ProgressBarFill,
                trackColor = PayUFinanceColors.ProgressBarTrack
            )
            
            Spacer(modifier = Modifier.height(Spacing30))
            
            // Amount Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    ElevateText(
                        markup = "Paid",
                        style = LpTypography.BodySmall,
                        color = PayUFinanceColors.ContentSecondary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    ElevateText(
                        markup = emiProgress.paidAmount,
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentPrimary
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
                        markup = emiProgress.remainingAmount,
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing30))
            
            // Installment Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevateText(
                    markup = "${emiProgress.paidInstallments}/${emiProgress.totalInstallments} Installments Paid",
                    style = LpTypography.BodySmall,
                    color = PayUFinanceColors.ContentSecondary
                )
                ElevateText(
                    markup = "${(emiProgress.progressPercentage * 100).toInt()}%",
                    style = LpTypography.TitleSecondary,
                    color = PayUFinanceColors.Primary
                )
            }
        }
    }
}

/**
 * Next Repayment Card Component
 */
@Composable
fun NextRepaymentCard(
    nextRepayment: NextRepaymentCard,
    onPayClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = if (nextRepayment.isOverdue) {
                PayUFinanceColors.CardBackgroundOverdue
            } else {
                PayUFinanceColors.CardBackground
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ElevateText(
                    markup = if (nextRepayment.isOverdue) "Overdue Payment" else "Next Repayment",
                    style = LpTypography.TitlePrimary,
                    color = if (nextRepayment.isOverdue) {
                        PayUFinanceColors.Error
                    } else {
                        PayUFinanceColors.ContentPrimary
                    },
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = nextRepayment.amount,
                    style = LpTypography.TitleSection,
                    color = PayUFinanceColors.ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = "Due: ${nextRepayment.dueDate}",
                    style = LpTypography.BodyNormal,
                    color = PayUFinanceColors.ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                nextRepayment.daysRemaining?.let { days ->
                    ElevateText(
                        markup = if (days > 0) "$days days remaining" else "Due today",
                        style = LpTypography.BodySmall,
                        color = PayUFinanceColors.Primary
                    )
                }
            }
            
            if (nextRepayment.showRepaymentCta) {
                LPButton(
                    modifier = Modifier.padding(start = Spacing20),
                    text = "Pay",
                    state = ButtonState.Enabled,
                    backgroundColor = PayUFinanceColors.Primary,
                    pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
                    disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                    contentColor = PayUFinanceColors.BackgroundPrimary,
                    pressedContentColor = PayUFinanceColors.BackgroundPrimary,
                    disabledContentColor = PayUFinanceColors.ContentTertiary,
                    circularProgressColor = PayUFinanceColors.BackgroundPrimary,
                    buttonElevation = ButtonDefaults.elevation(),
                    onClick = onPayClick
                )
            }
        }
    }
}

/**
 * Due Card Component (shown when there are overdue payments)
 */
@Composable
fun DueCard(
    dueCard: DueCard,
    onViewAllClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = if (dueCard.isUrgent) {
                PayUFinanceColors.CardBackgroundOverdue
            } else {
                PayUFinanceColors.CardBackground
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                ElevateText(
                    markup = "Total Due",
                    style = LpTypography.TitlePrimary,
                    color = if (dueCard.isUrgent) PayUFinanceColors.Error else PayUFinanceColors.ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = dueCard.totalDueAmount,
                    style = LpTypography.TitleSection,
                    color = if (dueCard.isUrgent) PayUFinanceColors.Error else PayUFinanceColors.ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = "${dueCard.overdueCount} ${if (dueCard.overdueCount == 1) "payment" else "payments"} overdue",
                    style = LpTypography.BodyNormal,
                    color = PayUFinanceColors.ContentSecondary
                )
            }
            TextButton(onClick = onViewAllClick) {
                ElevateText(
                    markup = "View All",
                    style = LpTypography.TitleSecondary,
                    color = PayUFinanceColors.Primary
                )
            }
        }
    }
}

/**
 * All EMIs Section Item
 */
@Composable
fun EmiItemCard(
    emiItem: EmiItem,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing20),
                    modifier = Modifier.padding(bottom = Spacing10)
                ) {
                    ElevateText(
                        markup = emiItem.installmentNumber,
                        style = LpTypography.TitleSecondary,
                        color = PayUFinanceColors.ContentPrimary
                    )
                    StatusChip(status = emiItem.status)
                }
                ElevateText(
                    markup = emiItem.amount,
                    style = LpTypography.BodyNormal,
                    color = PayUFinanceColors.ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = "Due: ${emiItem.dueDate}",
                    style = LpTypography.BodySmall,
                    color = PayUFinanceColors.ContentSecondary
                )
            }
        }
    }
}

/**
 * Status Chip for EMI Status
 */
@Composable
fun StatusChip(
    status: EmiStatus,
    modifier: Modifier = Modifier
) {
    val (text, textColor, bgColor) = when (status) {
        EmiStatus.PAID -> Triple("Paid", PayUFinanceColors.Success, PayUFinanceColors.SuccessBackground)
        EmiStatus.PENDING -> Triple("Pending", PayUFinanceColors.Warning, PayUFinanceColors.WarningBackground)
        EmiStatus.OVERDUE -> Triple("Overdue", PayUFinanceColors.Error, PayUFinanceColors.ErrorBackground)
        EmiStatus.PARTIALLY_PAID -> Triple("Partial", PayUFinanceColors.Primary, PayUFinanceColors.PrimaryLight)
    }
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Spacing20),
        color = bgColor
    ) {
        ElevateText(
            markup = text,
            style = LpTypography.SubtitleSecondary,
            color = textColor,
            modifier = Modifier.padding(horizontal = Spacing20, vertical = Spacing10)
        )
    }
}

