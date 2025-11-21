package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.toResource
import com.payu.finance.domain.usecase.GetLoansUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.LoanUiModel
import com.payu.finance.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for Loans screen
 */
data class LoansUiState(
    val loans: List<LoanUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * UI Events for Loans screen
 */
sealed class LoansEvent {
    object LoadLoans : LoansEvent()
    data class Retry(val action: String) : LoansEvent()
}

/**
 * ViewModel for Loans screen
 */
class LoansViewModel(
    private val getLoansUseCase: GetLoansUseCase
) : BaseViewModel<LoansUiState, LoansEvent>() {

    private val _loansResource = MutableStateFlow<Resource<List<LoanUiModel>>>(Resource.Loading())
    val loansResource: StateFlow<Resource<List<LoanUiModel>>> = _loansResource.asStateFlow()

    override fun createInitialState(): LoansUiState {
        return LoansUiState()
    }

    override fun handleEvent(event: LoansEvent) {
        when (event) {
            is LoansEvent.LoadLoans -> loadLoans()
            is LoansEvent.Retry -> loadLoans()
        }
    }

    init {
        loadLoans()
    }

    private fun loadLoans() {
        execute {
            _loansResource.value = Resource.Loading()
            val result = getLoansUseCase()
            _loansResource.value = result.toResource().let { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(resource.data?.map { it.toUiModel() } ?: emptyList())
                    is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }

    override fun handleError(exception: Exception) {
        _loansResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

