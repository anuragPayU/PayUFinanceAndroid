package com.payu.finance.ui.viewmodel

import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.screen.PaymentOption

/**
 * UI State for Repayment screen
 */
data class RepaymentUiState(
    val selectedPaymentOption: PaymentOption? = null,
    val isBottomSheetVisible: Boolean = false
)

/**
 * UI Events for Repayment screen
 */
sealed class RepaymentEvent {
    data class SelectPaymentOption(val option: PaymentOption) : RepaymentEvent()
    object ShowBottomSheet : RepaymentEvent()
    object DismissBottomSheet : RepaymentEvent()
    object ProceedWithPayment : RepaymentEvent()
}

/**
 * ViewModel for Repayment screen
 */
class RepaymentViewModel : BaseViewModel<RepaymentUiState, RepaymentEvent>() {

    override fun createInitialState(): RepaymentUiState {
        return RepaymentUiState()
    }

    override fun handleEvent(event: RepaymentEvent) {
        when (event) {
            is RepaymentEvent.SelectPaymentOption -> {
                _uiState.value = _uiState.value.copy(
                    selectedPaymentOption = event.option,
                    isBottomSheetVisible = true
                )
            }
            is RepaymentEvent.ShowBottomSheet -> {
                _uiState.value = _uiState.value.copy(
                    isBottomSheetVisible = true
                )
            }
            is RepaymentEvent.DismissBottomSheet -> {
                _uiState.value = _uiState.value.copy(
                    isBottomSheetVisible = false
                )
            }
            is RepaymentEvent.ProceedWithPayment -> {
                // Handle payment processing
                // This would typically trigger a use case or API call
                _uiState.value = _uiState.value.copy(
                    isBottomSheetVisible = false
                )
            }
        }
    }

    override fun handleError(exception: Exception) {
        // Handle errors
    }
}

