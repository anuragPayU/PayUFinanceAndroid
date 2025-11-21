package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.toResource
import com.payu.finance.domain.model.RepaymentStatus
import com.payu.finance.domain.usecase.GetRepaymentsUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.EmiStatus
import com.payu.finance.ui.screen.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI Events for History screen
 */
sealed class HistoryEvent {
    object LoadHistory : HistoryEvent()
    object Refresh : HistoryEvent()
}

/**
 * ViewModel for History screen
 */
class HistoryViewModel(
    private val getRepaymentsUseCase: GetRepaymentsUseCase
) : BaseViewModel<Unit, HistoryEvent>() {

    private val _historyResource = MutableStateFlow<Resource<List<HistoryItem>>>(Resource.Loading())
    val historyResource: StateFlow<Resource<List<HistoryItem>>> = _historyResource.asStateFlow()

    override fun createInitialState(): Unit {
        return Unit
    }

    override fun handleEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.LoadHistory -> loadHistory()
            is HistoryEvent.Refresh -> loadHistory()
        }
    }

    init {
        loadHistory()
    }

    fun loadHistory() {
        execute {
            _historyResource.value = Resource.Loading()
            val result = getRepaymentsUseCase().toResource()
            _historyResource.value = result.let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val repayments = resource.data ?: emptyList()
                        val historyItems = repayments.map { repayment ->
                            HistoryItem(
                                id = repayment.id,
                                title = "EMI Payment",
                                description = "Loan #${repayment.loanId}",
                                amount = formatCurrency(repayment.amount),
                                date = formatDate(repayment.dueDate),
                                status = when (repayment.status) {
                                    RepaymentStatus.PAID -> EmiStatus.PAID
                                    RepaymentStatus.PENDING -> EmiStatus.PENDING
                                    RepaymentStatus.OVERDUE -> EmiStatus.OVERDUE
                                    RepaymentStatus.PARTIALLY_PAID -> EmiStatus.PARTIALLY_PAID
                                },
                                loanId = repayment.loanId
                            )
                        }.sortedByDescending { it.date }
                        Resource.Success(historyItems)
                    }
                    is Resource.Error -> Resource.Error(resource.message ?: "Failed to load history")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }

    private fun formatCurrency(amount: Double): String {
        return "₹${String.format("%.2f", amount)}"
    }

    private fun formatDate(dateString: String): String {
        // TODO: Implement proper date formatting
        return dateString
    }

    override fun handleError(exception: Exception) {
        _historyResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

