package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.common.toResource
import com.payu.finance.domain.repository.ScreenContentRepository
import com.payu.finance.domain.usecase.VerifyOtpUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.OtpScreenData
import com.payu.finance.ui.model.toUiModel
import com.payu.finance.domain.model.OtpScreenContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for OTP screen
 */
data class OtpUiState(
    val otp: String = "",
    val mobileNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOtpComplete: Boolean = false,
    val content: OtpScreenData? = null,
    val isLoadingContent: Boolean = false
)

/**
 * UI Events for OTP screen
 */
sealed class OtpEvent {
    data class OtpChanged(val otp: String) : OtpEvent()
    object VerifyOtp : OtpEvent()
    object ClearError : OtpEvent()
}

/**
 * ViewModel for OTP verification screen
 */
class OtpViewModel(
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val screenContentRepository: ScreenContentRepository,
    val mobileNumber: String
) : BaseViewModel<OtpUiState, OtpEvent>() {

    private val _verifyOtpResource = MutableStateFlow<Resource<String>>(Resource.Loading())
    val verifyOtpResource: StateFlow<Resource<String>> = _verifyOtpResource.asStateFlow()

    override fun createInitialState(): OtpUiState {
        return OtpUiState(mobileNumber = mobileNumber)
    }
    
    init {
        loadScreenContent()
    }
    
    /**
     * Load screen content from Repository
     */
    private fun loadScreenContent() {
        execute {
            _uiState.value = _uiState.value.copy(isLoadingContent = true)
            when (val result = screenContentRepository.getOtpScreenContent()) {
                is Result.Success -> {
                    val domainContent: OtpScreenContent = result.data
                    _uiState.value = _uiState.value.copy(
                        content = domainContent.toUiModel(),
                        isLoadingContent = false
                    )
                }
                is Result.Error -> {
                    // On error, use default content (fallback)
                    _uiState.value = _uiState.value.copy(
                        isLoadingContent = false
                    )
                }
                is Result.Loading -> {
                    // Keep loading state
                    _uiState.value = _uiState.value.copy(isLoadingContent = true)
                }
            }
        }
    }

    override fun handleEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.OtpChanged -> {
                updateOtp(event.otp)
            }
            is OtpEvent.VerifyOtp -> {
                verifyOtp()
            }
            is OtpEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun updateOtp(otp: String) {
        val cleanedOtp = otp.filter { it.isDigit() }.take(6)
        val isComplete = cleanedOtp.length == 6
        
        _uiState.value = _uiState.value.copy(
            otp = cleanedOtp,
            isOtpComplete = isComplete,
            error = null
        )
    }

    private fun verifyOtp() {
        val otp = _uiState.value.otp
        val content = _uiState.value.content
        if (otp.length != 6) {
            _uiState.value = _uiState.value.copy(
                error = content?.errorMessages?.incompleteOtp 
                    ?: "Please enter complete 6-digit OTP"
            )
            return
        }

        execute {
            _verifyOtpResource.value = Resource.Loading()
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = verifyOtpUseCase(mobileNumber, otp)
            val content = _uiState.value.content
            _verifyOtpResource.value = result.toResource().let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Resource.Success(resource.data?.token ?: "")
                    }
                    is Resource.Error -> {
                        val errorMessage = resource.message ?: content?.errorMessages?.networkError 
                            ?: "Network error. Please try again."
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                        Resource.Error(errorMessage)
                    }
                    is Resource.Loading -> Resource.Loading()
                }
            }
            
            if (_verifyOtpResource.value !is Resource.Error) {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    override fun handleError(exception: Exception) {
        val content = _uiState.value.content
        val errorMessage = exception.message ?: content?.errorMessages?.genericError 
            ?: "An error occurred"
        _verifyOtpResource.value = Resource.Error(errorMessage)
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = errorMessage
        )
    }
}

