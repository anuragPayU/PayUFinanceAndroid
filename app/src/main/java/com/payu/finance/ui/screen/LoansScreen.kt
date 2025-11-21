package com.payu.finance.ui.screen

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
import com.payu.finance.common.Resource
import com.payu.finance.ui.model.LoanStatusUi
import com.payu.finance.ui.model.LoanUiModel
import com.payu.finance.ui.viewmodel.LoansEvent
import com.payu.finance.ui.viewmodel.LoansViewModel

/**
 * Composable screen for displaying loans
 */
@Composable
fun LoansScreen(
    viewModel: LoansViewModel,
    modifier: Modifier = Modifier
) {
    val loansResource by viewModel.loansResource.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "My Loans",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (val resource = loansResource) {
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
                    onRetry = { viewModel.handleEvent(LoansEvent.Retry("load_loans")) }
                )
            }
            is Resource.Success -> {
                LoansList(loans = resource.data ?: emptyList())
            }
        }
    }
}

@Composable
fun LoansList(loans: List<LoanUiModel>) {
    if (loans.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No loans found")
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(loans) { loan ->
                LoanCard(loan = loan)
            }
        }
    }
}

@Composable
fun LoanCard(loan: LoanUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = loan.amount,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Interest Rate: ${loan.interestRate}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tenure: ${loan.tenure}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${loan.status.name}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Remaining: ${loan.remainingAmount}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

// Preview Composable
@Preview(showBackground = true, name = "Loans Screen Preview")
@Composable
private fun LoansScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
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
            ),
            LoanUiModel(
                id = "loan_3",
                amount = "₹75,000",
                interestRate = "15%",
                tenure = "9 months",
                status = LoanStatusUi.OVERDUE,
                disbursementDate = "01 Mar 2023",
                dueDate = "15 Dec 2023",
                remainingAmount = "₹25,000"
            )
        )
        
        LoansList(loans = mockLoans)
    }
}

