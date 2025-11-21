package com.payu.finance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.payu.finance.ui.model.*
import com.payu.finance.ui.screen.auth.MobileInputField
import com.payu.finance.ui.screen.auth.OtpInputField
import com.payu.finance.ui.theme.PayUFinanceTheme

/**
 * Comprehensive Preview Screen
 * This screen displays all UI screens for verification
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("UI Screens Preview") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Home Screen Preview
            item {
                PreviewSection(
                    title = "Home Screen",
                    content = {
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
                )
            }

            // Loans Screen Preview
            item {
                PreviewSection(
                    title = "Loans Screen",
                    content = {
                        val mockLoans = listOf(
                            LoanUiModel(
                                id = "loan_1",
                                amount = "₹1,00,000",
                                interestRate = "12%",
                                tenure = "12 months",
                                status = LoanStatusUi.ACTIVE,
                                disbursementDate = "01 Jan 2023",
                                dueDate = "15 Jan 2024",
                                remainingAmount = "₹60,000"
                            ),
                            LoanUiModel(
                                id = "loan_2",
                                amount = "₹50,000",
                                interestRate = "10%",
                                tenure = "6 months",
                                status = LoanStatusUi.COMPLETED,
                                disbursementDate = "01 Jun 2023",
                                dueDate = null,
                                remainingAmount = "₹0"
                            )
                        )
                        LoansList(loans = mockLoans)
                    }
                )
            }

            // Repayments Screen Preview
            item {
                PreviewSection(
                    title = "Repayments Screen",
                    content = {
                        val mockRepayments = listOf(
                            RepaymentUiModel(
                                id = "repayment_1",
                                loanId = "loan_1",
                                amount = "₹8,333",
                                dueDate = "15 Dec 2023",
                                paidDate = "14 Dec 2023",
                                status = RepaymentStatusUi.PAID,
                                installmentNumber = "#1"
                            ),
                            RepaymentUiModel(
                                id = "repayment_2",
                                loanId = "loan_1",
                                amount = "₹8,333",
                                dueDate = "15 Jan 2024",
                                paidDate = null,
                                status = RepaymentStatusUi.PENDING,
                                installmentNumber = "#2"
                            )
                        )
                        RepaymentsList(repayments = mockRepayments)
                    }
                )
            }

            // Loan Detail Screen Preview
            item {
                PreviewSection(
                    title = "Loan Detail Screen",
                    content = {
                        val mockLoanDetail = LoanDetailUiState(
                            sections = listOf(
                                LoanDetailSectionUiItem.DetailCard(
                                    title = "Loan Amount",
                                    subtitle = "₹1,00,000",
                                    statusLabel = "Active",
                                    statusColor = "#4CAF50"
                                ),
                                LoanDetailSectionUiItem.EmiDetail(
                                    title = "EMI Details",
                                    header = EmiDetailHeader(
                                        title = "Next EMI",
                                        subtitle = "₹8,333",
                                        percentage = "40%"
                                    ),
                                    rows = listOf(
                                        EmiDetailRow(
                                            title = "Total Installments",
                                            subtitle = "12"
                                        ),
                                        EmiDetailRow(
                                            title = "Paid Installments",
                                            subtitle = "5"
                                        )
                                    ),
                                    primaryAction = ActionItem(
                                        text = "Pay Now",
                                        type = "REPAYMENT",
                                        url = null
                                    )
                                ),
                                LoanDetailSectionUiItem.RowListCard(
                                    title = "Documents",
                                    items = listOf(
                                        ActionableCardItem(
                                            title = "Loan Agreement",
                                            action = ActionItem(
                                                text = "Download",
                                                type = "DOWNLOADABLE",
                                                url = "https://example.com/loan_agreement.pdf"
                                            )
                                        ),
                                        ActionableCardItem(
                                            title = "Sanction Letter",
                                            action = ActionItem(
                                                text = "Download",
                                                type = "DOWNLOADABLE",
                                                url = "https://example.com/sanction_letter.pdf"
                                            )
                                        )
                                    )
                                )
                            )
                        )
                        LoanDetailContent(
                            loanDetail = mockLoanDetail,
                            onDownloadClick = { url, fileName -> },
                            onActionClick = { action -> }
                        )
                    }
                )
            }

            // Mobile Input Screen Preview
            item {
                PreviewSection(
                    title = "Mobile Input Screen",
                    content = {
                        val mockContent = EnterMobileScreenData(
                            title = "Enter Your Mobile Number",
                            subtitle = "We'll send you an OTP to verify your number",
                            mobileNumberLabel = "Mobile Number",
                            mobileNumberPlaceholder = "Enter 10-digit mobile number",
                            continueButtonText = "Continue",
                            consentText = ConsentTextData(
                                prefixText = "By continuing, you agree to our ",
                                privacyPolicyLinkText = "Privacy Policy",
                                middleText = " and ",
                                termsAndConditionsLinkText = "Terms & Conditions",
                                privacyPolicyUrl = "https://example.com/privacy",
                                termsAndConditionsUrl = "https://example.com/terms"
                            ),
                            errorMessages = ErrorMessagesData(
                                invalidMobileNumber = "Please enter a valid 10-digit mobile number",
                                networkError = "Network error. Please try again.",
                                genericError = "An error occurred. Please try again."
                            ),
                            countryCode = "+91"
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = mockContent.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = mockContent.subtitle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            MobileInputField(
                                mobileNumber = "9876543210",
                                error = null,
                                label = mockContent.mobileNumberLabel,
                                placeholder = mockContent.mobileNumberPlaceholder,
                                countryCode = mockContent.countryCode,
                                onMobileNumberChanged = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                )
            }

            // OTP Screen Preview
            item {
                PreviewSection(
                    title = "OTP Screen",
                    content = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Enter OTP",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "We've sent an OTP to +91 9876543210",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            OtpInputField(
                                otp = "123456",
                                error = null,
                                onOtpChanged = {},
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                )
            }
        }
    }
}

/**
 * Preview Section Wrapper
 */
@Composable
private fun PreviewSection(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Divider()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp)
            ) {
                content()
            }
        }
    }
}

// Preview Composable for PreviewScreen
@Preview(showBackground = true, name = "All Screens Preview", showSystemUi = true)
@Composable
private fun PreviewScreenPreview() {
    PayUFinanceTheme {
        PreviewScreen()
    }
}

