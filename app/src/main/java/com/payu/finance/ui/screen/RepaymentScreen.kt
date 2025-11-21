package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import androidx.compose.material.ButtonDefaults
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.viewmodel.RepaymentEvent
import com.payu.finance.ui.viewmodel.RepaymentViewModel
import kotlinx.coroutines.launch

/**
 * Repayment Screen with Radio Cards and Bottom Sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepaymentScreen(
    viewModel: RepaymentViewModel,
    loanId: String? = null,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    // Expand bottom sheet when it becomes visible
    LaunchedEffect(uiState.isBottomSheetVisible) {
        if (uiState.isBottomSheetVisible) {
            coroutineScope.launch {
                bottomSheetState.expand()
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), // Disable automatic window insets for edge-to-edge
        topBar = {
            TopAppBar(
                title = {
                    ElevateText(
                        markup = "Repayment",
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
                .statusBarsPadding() // Only add status bar padding
                .padding(paddingValues) // Keep Scaffold padding for TopAppBar
        ) {
            RepaymentContent(
                selectedOption = uiState.selectedPaymentOption,
                onPaymentOptionSelected = { option ->
                    viewModel.handleEvent(RepaymentEvent.SelectPaymentOption(option))
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // Bottom Sheet
    if (uiState.isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.handleEvent(RepaymentEvent.DismissBottomSheet)
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            },
            sheetState = bottomSheetState,
            containerColor = PayUFinanceColors.BackgroundPrimary,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(PayUFinanceColors.BorderPrimary)
                )
                Spacer(modifier = Modifier.height(Spacing20))
            }
        ) {
            RepaymentBottomSheetContent(
                selectedOption = uiState.selectedPaymentOption,
                onProceed = {
                    // Handle proceed action
                    viewModel.handleEvent(RepaymentEvent.ProceedWithPayment)
                },
                onDismiss = {
                    viewModel.handleEvent(RepaymentEvent.DismissBottomSheet)
                    coroutineScope.launch {
                        bottomSheetState.hide()
                    }
                }
            )
        }
    }
}

/**
 * Repayment Content with Radio Cards
 */
@Composable
fun RepaymentContent(
    selectedOption: PaymentOption?,
    onPaymentOptionSelected: (PaymentOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing40),
        verticalArrangement = Arrangement.spacedBy(Spacing30)
    ) {
        // Title
        ElevateText(
            markup = "Choose Payment Option",
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing10)
        )

        // Description
        ElevateText(
            markup = "Select how you want to make your repayment",
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentSecondary,
            modifier = Modifier.padding(bottom = Spacing30)
        )

        // Radio Cards
        PaymentOptionRadioCard(
            option = PaymentOption.FULL_PAYMENT,
            isSelected = selectedOption == PaymentOption.FULL_PAYMENT,
            onClick = { onPaymentOptionSelected(PaymentOption.FULL_PAYMENT) },
            modifier = Modifier.fillMaxWidth()
        )

        PaymentOptionRadioCard(
            option = PaymentOption.PARTIAL_PAYMENT,
            isSelected = selectedOption == PaymentOption.PARTIAL_PAYMENT,
            onClick = { onPaymentOptionSelected(PaymentOption.PARTIAL_PAYMENT) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Payment Option Radio Card
 */
@Composable
fun PaymentOptionRadioCard(
    option: PaymentOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PayUFinanceColors.Primary else PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            )
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PayUFinanceColors.PrimaryLight else PayUFinanceColors.BackgroundPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing10)
            ) {
                ElevateText(
                    markup = option.title,
                    style = LpTypography.TitleSecondary,
                    color = PayUFinanceColors.ContentPrimary
                )
                ElevateText(
                    markup = option.description,
                    style = LpTypography.BodySmall,
                    color = PayUFinanceColors.ContentSecondary
                )
            }

            // Radio Button
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PayUFinanceColors.Primary,
                    unselectedColor = PayUFinanceColors.BorderPrimary
                )
            )
        }
    }
}

/**
 * Repayment Bottom Sheet Content
 */
@Composable
fun RepaymentBottomSheetContent(
    selectedOption: PaymentOption?,
    onProceed: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing40)
            .padding(bottom = Spacing40),
        verticalArrangement = Arrangement.spacedBy(Spacing30)
    ) {
        // Title
        ElevateText(
            markup = selectedOption?.title ?: "Payment Details",
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary
        )

        // Description
        ElevateText(
            markup = selectedOption?.bottomSheetDescription ?: "Review your payment details",
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentSecondary
        )

        // Payment Details Card
        selectedOption?.let { option ->
            PaymentDetailsCard(
                option = option,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(Spacing20))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing20)
        ) {
            // Cancel Button
            LPButton(
                text = "Cancel",
                state = ButtonState.Enabled,
                backgroundColor = PayUFinanceColors.BackgroundSecondary,
                pressedBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                contentColor = PayUFinanceColors.ContentPrimary,
                pressedContentColor = PayUFinanceColors.ContentPrimary,
                disabledContentColor = PayUFinanceColors.ContentTertiary,
                circularProgressColor = PayUFinanceColors.ContentPrimary,
                buttonElevation = ButtonDefaults.elevation(),
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            )

            // Proceed Button
            LPButton(
                text = "Proceed",
                state = ButtonState.Enabled,
                backgroundColor = PayUFinanceColors.Primary,
                pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
                disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                contentColor = PayUFinanceColors.BackgroundPrimary,
                pressedContentColor = PayUFinanceColors.BackgroundPrimary,
                disabledContentColor = PayUFinanceColors.ContentTertiary,
                circularProgressColor = PayUFinanceColors.BackgroundPrimary,
                buttonElevation = ButtonDefaults.elevation(),
                onClick = onProceed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Payment Details Card
 */
@Composable
fun PaymentDetailsCard(
    option: PaymentOption,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.BackgroundSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            verticalArrangement = Arrangement.spacedBy(Spacing20)
        ) {
            PaymentDetailRow(
                label = "Payment Type",
                value = option.title
            )
            PaymentDetailRow(
                label = "Amount",
                value = option.amount
            )
            PaymentDetailRow(
                label = "Due Date",
                value = option.dueDate
            )
        }
    }
}

/**
 * Payment Detail Row
 */
@Composable
fun PaymentDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ElevateText(
            markup = label,
            style = LpTypography.BodySmall,
            color = PayUFinanceColors.ContentSecondary
        )
        ElevateText(
            markup = value,
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentPrimary,
            textAlign = TextAlign.End
        )
    }
}

/**
 * Payment Option Enum
 */
enum class PaymentOption(
    val title: String,
    val description: String,
    val bottomSheetDescription: String,
    val amount: String,
    val dueDate: String
) {
    FULL_PAYMENT(
        title = "Full Payment",
        description = "Pay the complete outstanding amount",
        bottomSheetDescription = "You are about to make a full payment. Please review the details below.",
        amount = "₹50,000",
        dueDate = "15 Jan 2024"
    ),
    PARTIAL_PAYMENT(
        title = "Partial Payment",
        description = "Pay a portion of the outstanding amount",
        bottomSheetDescription = "You are about to make a partial payment. Please review the details below.",
        amount = "₹25,000",
        dueDate = "15 Jan 2024"
    )
}

// Preview
@Preview(showBackground = true, name = "Repayment Screen Preview")
@Composable
private fun RepaymentScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        RepaymentContent(
            selectedOption = PaymentOption.FULL_PAYMENT,
            onPaymentOptionSelected = {}
        )
    }
}

