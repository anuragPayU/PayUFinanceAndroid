package com.payu.finance.ui.screen

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.payu.finance.common.Resource
import com.payu.finance.ui.components.StatusChip
import com.payu.finance.ui.model.EmiBreakdownItem
import com.payu.finance.ui.model.EmiStatus
import com.payu.finance.ui.model.LoanDetailUiState
import com.payu.finance.ui.model.LoanStatus
import com.payu.finance.ui.viewmodel.LoanDetailViewModel

/**
 * Loan Detail Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(
    viewModel: LoanDetailViewModel,
    loanId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(loanId) {
        viewModel.loadLoanDetails(loanId)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Loan Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val resource = uiState) {
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
                        onRetry = { viewModel.loadLoanDetails(loanId) }
                    )
                }
                is Resource.Success -> {
                    val loanDetail = resource.data
                    if (loanDetail != null) {
                        LoanDetailContent(
                            loanDetail = loanDetail,
                        onDownloadLoanAgreement = { url ->
                            val fileName = "Loan_Agreement_${loanDetail.loanId}.pdf"
                            if (downloadFile(context, url, fileName)) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Download started: $fileName")
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to start download")
                                }
                            }
                        },
                        onDownloadSanctionLetter = { url ->
                            val fileName = "Sanction_Letter_${loanDetail.loanId}.pdf"
                            if (downloadFile(context, url, fileName)) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Download started: $fileName")
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to start download")
                                }
                            }
                        }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Loan Detail Content
 */
@Composable
fun LoanDetailContent(
    loanDetail: com.payu.finance.ui.model.LoanDetailUiState,
    onDownloadLoanAgreement: (String) -> Unit,
    onDownloadSanctionLetter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Loan Overview Card
        item {
            LoanOverviewCard(loanDetail = loanDetail)
        }

        // Payment Summary Card
        item {
            PaymentSummaryCard(loanDetail = loanDetail)
        }

        // Next EMI Card (if applicable)
        loanDetail.nextEmiDate?.let { nextEmiDate ->
            item {
                NextEmiCard(
                    amount = loanDetail.nextEmiAmount ?: "",
                    dueDate = nextEmiDate
                )
            }
        }

        // EMI Breakdown Section
        if (loanDetail.emiBreakdown.isNotEmpty()) {
            item {
                Text(
                    text = "EMI Breakdown",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(
                count = loanDetail.emiBreakdown.size,
                key = { index -> loanDetail.emiBreakdown[index].installmentNumber }
            ) { index ->
                EmiBreakdownItemCard(emiItem = loanDetail.emiBreakdown[index])
            }
        }

        // Documents Section
        item {
            Text(
                text = "Documents",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Loan Agreement
        loanDetail.loanAgreementUrl?.let { url ->
            item {
                DocumentDownloadCard(
                    title = "Loan Agreement",
                    onDownloadClick = { onDownloadLoanAgreement(url) }
                )
            }
        }

        // Sanction Letter
        loanDetail.sanctionLetterUrl?.let { url ->
            item {
                DocumentDownloadCard(
                    title = "Sanction Letter",
                    onDownloadClick = { onDownloadSanctionLetter(url) }
                )
            }
        }
    }
}

/**
 * Loan Overview Card
 */
@Composable
fun LoanOverviewCard(
    loanDetail: com.payu.finance.ui.model.LoanDetailUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loan Amount",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                StatusChip(
                    status = when (loanDetail.status) {
                        LoanStatus.ACTIVE -> EmiStatus.PENDING
                        LoanStatus.COMPLETED -> EmiStatus.PAID
                        LoanStatus.OVERDUE -> EmiStatus.OVERDUE
                        LoanStatus.PENDING -> EmiStatus.PENDING
                    }
                )
            }

            Text(
                text = loanDetail.loanAmount,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Interest Rate",
                    value = loanDetail.interestRate
                )
                InfoItem(
                    label = "Tenure",
                    value = "${loanDetail.tenure} months"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Disbursement Date",
                    value = loanDetail.disbursementDate
                )
                loanDetail.dueDate?.let { dueDate ->
                    InfoItem(
                        label = "Due Date",
                        value = dueDate
                    )
                }
            }
        }
    }
}

/**
 * Payment Summary Card
 */
@Composable
fun PaymentSummaryCard(
    loanDetail: com.payu.finance.ui.model.LoanDetailUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Payment Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Paid Amount",
                    value = loanDetail.paidAmount
                )
                InfoItem(
                    label = "Remaining Amount",
                    value = loanDetail.remainingAmount
                )
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Paid Installments",
                    value = "${loanDetail.paidInstallments}/${loanDetail.totalInstallments}"
                )
                val progress = if (loanDetail.totalInstallments > 0) {
                    (loanDetail.paidInstallments.toFloat() / loanDetail.totalInstallments.toFloat() * 100).toInt()
                } else 0
                InfoItem(
                    label = "Progress",
                    value = "$progress%"
                )
            }

            LinearProgressIndicator(
                progress = if (loanDetail.totalInstallments > 0) {
                    loanDetail.paidInstallments.toFloat() / loanDetail.totalInstallments.toFloat()
                } else 0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

/**
 * Next EMI Card
 */
@Composable
fun NextEmiCard(
    amount: String,
    dueDate: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Next EMI",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = amount,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Due: $dueDate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * EMI Breakdown Item Card
 */
@Composable
fun EmiBreakdownItemCard(
    emiItem: com.payu.finance.ui.model.EmiBreakdownItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EMI ${emiItem.installmentNumber}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                StatusChip(status = emiItem.status)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = emiItem.amount,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Due Date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = emiItem.dueDate,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    label = "Principal",
                    value = emiItem.principalAmount
                )
                InfoItem(
                    label = "Interest",
                    value = emiItem.interestAmount
                )
            }
        }
    }
}

/**
 * Document Download Card
 */
@Composable
fun DocumentDownloadCard(
    title: String,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onDownloadClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Download",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Info Item Component
 */
@Composable
fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

/**
 * Download File Function
 * Returns true if download was successfully started, false otherwise
 */
fun downloadFile(context: Context, url: String, fileName: String): Boolean {
    return try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        if (downloadManager == null) {
            false
        } else {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Downloading $fileName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setMimeType("application/pdf")

            downloadManager.enqueue(request)
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Preview Composable
@Preview(showBackground = true, name = "Loan Detail Screen Preview")
@Composable
private fun LoanDetailScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        val mockLoanDetail = LoanDetailUiState(
            loanId = "loan_1",
            loanAmount = "₹1,00,000",
            interestRate = "12%",
            tenure = 12,
            status = LoanStatus.ACTIVE,
            disbursementDate = "01 Jan 2023",
            dueDate = "15 Jan 2024",
            remainingAmount = "₹60,000",
            paidAmount = "₹40,000",
            totalInstallments = 12,
            paidInstallments = 5,
            nextEmiDate = "15 Jan 2024",
            nextEmiAmount = "₹8,333",
            emiBreakdown = listOf(
                EmiBreakdownItem(
                    installmentNumber = 1,
                    amount = "₹8,333",
                    dueDate = "15 Dec 2023",
                    status = EmiStatus.PAID,
                    principalAmount = "₹7,333",
                    interestAmount = "₹1,000"
                ),
                EmiBreakdownItem(
                    installmentNumber = 2,
                    amount = "₹8,333",
                    dueDate = "15 Jan 2024",
                    status = EmiStatus.PENDING,
                    principalAmount = "₹7,333",
                    interestAmount = "₹1,000"
                ),
                EmiBreakdownItem(
                    installmentNumber = 3,
                    amount = "₹8,333",
                    dueDate = "15 Feb 2024",
                    status = EmiStatus.PENDING,
                    principalAmount = "₹7,333",
                    interestAmount = "₹1,000"
                )
            ),
            loanAgreementUrl = "https://example.com/loan_agreement.pdf",
            sanctionLetterUrl = "https://example.com/sanction_letter.pdf"
        )
        
        LoanDetailContent(
            loanDetail = mockLoanDetail,
            onDownloadLoanAgreement = {},
            onDownloadSanctionLetter = {}
        )
    }
}

