package com.payu.finance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.domain.usecase.GetLoanByIdUseCase
import com.payu.finance.ui.model.LoanDetailUiState
import com.payu.finance.ui.model.LoanStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoanDetailViewModel(
    private val getLoanByIdUseCase: GetLoanByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<LoanDetailUiState>>(Resource.Loading())
    val uiState: StateFlow<Resource<LoanDetailUiState>> = _uiState.asStateFlow()

    fun loadLoanDetails(loanId: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                when (val result = getLoanByIdUseCase(loanId)) {
                    is Result.Success -> {
                        val loan = result.data
                // Convert domain model to UI model
                // For now, using mock data structure - replace with actual API call
                val uiState = LoanDetailUiState(
                    loanId = loan.id,
                    loanAmount = formatCurrency(loan.amount),
                    interestRate = "${loan.interestRate}%",
                    tenure = loan.tenure,
                    status = when (loan.status) {
                        com.payu.finance.domain.model.LoanStatus.ACTIVE -> LoanStatus.ACTIVE
                        com.payu.finance.domain.model.LoanStatus.COMPLETED -> LoanStatus.COMPLETED
                        com.payu.finance.domain.model.LoanStatus.PENDING -> LoanStatus.PENDING
                        com.payu.finance.domain.model.LoanStatus.OVERDUE -> LoanStatus.OVERDUE
                    },
                    disbursementDate = loan.disbursementDate,
                    dueDate = loan.dueDate,
                    remainingAmount = formatCurrency(loan.remainingAmount),
                    paidAmount = formatCurrency(loan.amount - loan.remainingAmount),
                    totalInstallments = loan.tenure,
                    paidInstallments = calculatePaidInstallments(loan),
                    nextEmiDate = loan.dueDate,
                    nextEmiAmount = calculateNextEmiAmount(loan),
                    loanAgreementUrl = "https://example.com/loan-agreement.pdf", // Replace with actual URL
                    sanctionLetterUrl = "https://example.com/sanction-letter.pdf" // Replace with actual URL
                )
                        _uiState.value = Resource.Success(uiState)
                    }
                    is Result.Error -> {
                        _uiState.value = Resource.Error(result.exception.message ?: "Failed to load loan details")
                    }
                    is Result.Loading -> {
                        _uiState.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                _uiState.value = Resource.Error(e.message ?: "Failed to load loan details")
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        return "₹${String.format("%.2f", amount)}"
    }

    private fun calculatePaidInstallments(loan: com.payu.finance.domain.model.Loan): Int {
        // Calculate based on remaining amount
        val paidRatio = (loan.amount - loan.remainingAmount) / loan.amount
        return (loan.tenure * paidRatio).toInt()
    }

    private fun calculateNextEmiAmount(loan: com.payu.finance.domain.model.Loan): String {
        // Simple calculation - replace with actual EMI calculation
        val monthlyEmi = loan.amount / loan.tenure
        return formatCurrency(monthlyEmi)
    }
}

