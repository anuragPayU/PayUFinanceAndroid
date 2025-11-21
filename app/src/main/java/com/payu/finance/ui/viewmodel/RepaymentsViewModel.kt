package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.toResource
import com.payu.finance.domain.usecase.GetRepaymentsUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.RepaymentUiModel
import com.payu.finance.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for Repayments screen
 */
data class RepaymentsUiState(
    val repayments: List<RepaymentUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedLoanId: String? = null
)

/**
 * UI Events for Repayments screen
 */
sealed class RepaymentsEvent {
    object LoadRepayments : RepaymentsEvent()
    data class LoadRepaymentsForLoan(val loanId: String) : RepaymentsEvent()
    data class Retry(val action: String) : RepaymentsEvent()
}

/**
 * ViewModel for Repayments screen
 */
class RepaymentsViewModel(
    private val getRepaymentsUseCase: GetRepaymentsUseCase
) : BaseViewModel<RepaymentsUiState, RepaymentsEvent>() {

    private val _repaymentsResource = MutableStateFlow<Resource<List<RepaymentUiModel>>>(Resource.Loading())
    val repaymentsResource: StateFlow<Resource<List<RepaymentUiModel>>> = _repaymentsResource.asStateFlow()

    override fun createInitialState(): RepaymentsUiState {
        return RepaymentsUiState()
    }

    override fun handleEvent(event: RepaymentsEvent) {
        when (event) {
            is RepaymentsEvent.LoadRepayments -> loadRepayments()
            is RepaymentsEvent.LoadRepaymentsForLoan -> loadRepayments(event.loanId)
            is RepaymentsEvent.Retry -> loadRepayments(_uiState.value.selectedLoanId)
        }
    }

    init {
        loadRepayments()
    }

    private fun loadRepayments(loanId: String? = null) {
        execute {
            _repaymentsResource.value = Resource.Loading()
            val result = getRepaymentsUseCase(loanId)
            _repaymentsResource.value = result.toResource().let { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data?.map { it.toUiModel() } ?: emptyList())
                    is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                    is Resource.Loading -> Resource.Loading()
                }
            }
            _uiState.value = _uiState.value.copy(selectedLoanId = loanId)
        }
    }

    override fun handleError(exception: Exception) {
        _repaymentsResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

