package com.payu.finance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.AccountBalance
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
        shape = RoundedCornerShape(16.dp), // Updated to match Figma design
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) // Updated padding to match Figma
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
 * Column layout with full-width button as per Figma design
 */
@Composable
fun NextRepaymentCard(
    nextRepayment: NextRepaymentCard,
    onPayClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp), // Updated to match Figma design
        colors = CardDefaults.cardColors(
            containerColor = if (nextRepayment.isOverdue) {
                PayUFinanceColors.CardBackgroundOverdue
            } else {
                PayUFinanceColors.BackgroundPrimary // White background
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) // Updated padding to match Figma
        ) {
            // Title
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
            
            // Amount
            ElevateText(
                markup = nextRepayment.amount,
                style = LpTypography.TitleSection,
                color = PayUFinanceColors.ContentPrimary,
                modifier = Modifier.padding(bottom = Spacing10)
            )
            
            // Due date
            ElevateText(
                markup = "Due: ${nextRepayment.dueDate}",
                style = LpTypography.BodyNormal,
                color = PayUFinanceColors.ContentSecondary,
                modifier = Modifier.padding(bottom = Spacing10)
            )
            
            // Days remaining
            nextRepayment.daysRemaining?.let { days ->
                ElevateText(
                    markup = if (days > 0) "$days days remaining" else "Due today",
                    style = LpTypography.BodySmall,
                    color = PayUFinanceColors.Primary,
                    modifier = Modifier.padding(bottom = Spacing20)
                )
            }
            
            // Add spacing before button if daysRemaining is null
            if (nextRepayment.daysRemaining == null) {
                Spacer(modifier = Modifier.height(Spacing10))
            }
            
            // CTA Button - Full width
            if (nextRepayment.showRepaymentCta) {
                LPButton(
                    modifier = Modifier.fillMaxWidth(),
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
 * Red background with white text, info icon, and full-width CTA button as per Figma design
 */
@Composable
fun DueCard(
    dueCard: DueCard,
    onPayClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp), // Updated to match Figma design
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.Error // Red background as per Figma
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp) // Updated padding to match Figma
        ) {
            // Title row with info icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing10)
                ) {
                    ElevateText(
                        markup = "Total Due",
                        style = LpTypography.TitlePrimary,
                        color = ContentInversePrimary, // White text as per Figma
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(onClick = onInfoClick),
                        tint = ContentInversePrimary // White icon as per Figma
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Spacing10))
            
            // Amount
            ElevateText(
                markup = dueCard.totalDueAmount,
                style = LpTypography.TitleSection,
                color = ContentInversePrimary, // White text as per Figma
                modifier = Modifier.padding(bottom = Spacing10)
            )
            
            // Overdue count
            ElevateText(
                markup = "${dueCard.overdueCount} ${if (dueCard.overdueCount == 1) "payment" else "payments"} overdue",
                style = LpTypography.BodyNormal,
                color = ContentInversePrimary, // White text as per Figma
                modifier = Modifier.padding(bottom = Spacing20)
            )
            
            // CTA Button - Full width
            LPButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Pay Now",
                state = ButtonState.Enabled,
                backgroundColor = ContentInversePrimary, // White background for button
                pressedBackgroundColor = PayUFinanceColors.BackgroundSecondary,
                disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                contentColor = PayUFinanceColors.Error, // Red text on white button
                pressedContentColor = PayUFinanceColors.Error,
                disabledContentColor = PayUFinanceColors.ContentTertiary,
                circularProgressColor = PayUFinanceColors.Error,
                buttonElevation = ButtonDefaults.elevation(),
                onClick = onPayClick
            )
        }
    }
}

/**
 * Grouped EMI List Card
 * Single card container with borders and rounded corners only on first and last items
 */
@Composable
fun EmiListGroupedCard(
    emis: List<EmiItem>,
    onItemClick: (EmiItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (emis.isEmpty()) return
    
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
            emis.forEachIndexed { index, emiItem ->
                EmiItemContent(
                    emiItem = emiItem,
                    onClick = { onItemClick(emiItem) },
                    showDivider = index < emis.size - 1
                )
            }
        }
    }
}

/**
 * EMI Item Content (without card wrapper, used inside grouped card)
 * Updated layout to match Figma design
 */
@Composable
private fun EmiItemContent(
    emiItem: EmiItem,
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
            // Left side - Icon and Content
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(Spacing20),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Loan",
                    tint = PayUFinanceColors.ContentSecondary,
                    modifier = Modifier.size(48.dp)
                )
                
                // Content
                Column(modifier = Modifier.weight(1f)) {
                    // Amount - Title (on top)
                    ElevateText(
                        markup = emiItem.amount,
                        style = LpTypography.TitleSection,
                        color = PayUFinanceColors.ContentPrimary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    
                    // Installment number - Subtitle (below amount)
                    ElevateText(
                        markup = emiItem.installmentNumber,
                        style = LpTypography.TitleSecondary,
                        color = PayUFinanceColors.ContentPrimary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    
//                    // Due date (without "Due:" prefix)
//                    ElevateText(
//                        markup = emiItem.dueDate,
//                        style = LpTypography.BodySmall,
//                        color = PayUFinanceColors.ContentSecondary,
//                        modifier = Modifier.padding(bottom = Spacing10)
//                    )
                    
                    // Status chip - Label (at bottom)
                    StatusChip(status = emiItem.status)
                }
            }
            
            // Right side - Chevron icon indicating clickable
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View details",
                tint = PayUFinanceColors.ContentTertiary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Divider between items (not after last item)
        if (showDivider) {
            HorizontalDivider(
                color = PayUFinanceColors.BorderPrimary,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

/**
 * All EMIs Section Item (legacy - kept for backward compatibility)
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
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(16.dp)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
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

