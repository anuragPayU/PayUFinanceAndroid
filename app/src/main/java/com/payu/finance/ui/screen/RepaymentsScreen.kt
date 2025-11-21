package com.payu.finance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.payu.finance.common.Resource
import com.payu.finance.ui.model.RepaymentStatusUi
import com.payu.finance.ui.model.RepaymentUiModel
import com.payu.finance.ui.viewmodel.RepaymentsEvent
import com.payu.finance.ui.viewmodel.RepaymentsViewModel

/**
 * Composable screen for displaying repayments
 */
@Composable
fun RepaymentsScreen(
    viewModel: RepaymentsViewModel,
    loanId: String? = null,
    modifier: Modifier = Modifier
) {
    val repaymentsResource by viewModel.repaymentsResource.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Text(
            text = if (loanId != null) "Repayments for Loan" else "All Repayments",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val resource = repaymentsResource) {
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
                    onRetry = { viewModel.handleEvent(RepaymentsEvent.Retry("load_repayments")) }
                )
            }
            is Resource.Success -> {
                RepaymentsList(repayments = resource.data ?: emptyList())
            }
        }
    }
}

@Composable
fun RepaymentsList(repayments: List<RepaymentUiModel>) {
    if (repayments.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No repayments found")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = repayments,
                key = { it.id }
            ) { repayment ->
                RepaymentCard(repayment = repayment)
            }
        }
    }
}

@Composable
fun RepaymentCard(repayment: RepaymentUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = repayment.installmentNumber,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = repayment.amount,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Due Date: ${repayment.dueDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            repayment.paidDate?.let {
                Text(
                    text = "Paid Date: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "Status: ${repayment.status.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Preview Composable
@Preview(showBackground = true, name = "Repayments Screen Preview")
@Composable
private fun RepaymentsScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
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
            ),
            RepaymentUiModel(
                id = "repayment_3",
                loanId = "loan_1",
                amount = "₹8,333",
                dueDate = "15 Feb 2024",
                paidDate = null,
                status = RepaymentStatusUi.PENDING,
                installmentNumber = "#3"
            ),
            RepaymentUiModel(
                id = "repayment_4",
                loanId = "loan_2",
                amount = "₹5,000",
                dueDate = "10 Dec 2023",
                paidDate = null,
                status = RepaymentStatusUi.OVERDUE,
                installmentNumber = "#1"
            )
        )
        
        RepaymentsList(repayments = mockRepayments)
    }
}

