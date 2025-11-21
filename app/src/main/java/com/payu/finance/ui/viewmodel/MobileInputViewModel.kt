package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.common.toResource
import com.payu.finance.domain.repository.ScreenContentRepository
import com.payu.finance.domain.usecase.SendOtpUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.EnterMobileScreenData
import com.payu.finance.ui.model.toUiModel
import com.payu.finance.domain.model.MobileInputScreenContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for Mobile Input screen
 */
data class MobileInputUiState(
    val mobileNumber: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMobileValid: Boolean = false,
    val content: EnterMobileScreenData? = null,
    val isLoadingContent: Boolean = false
)

/**
 * UI Events for Mobile Input screen
 */
sealed class MobileInputEvent {
    data class MobileNumberChanged(val mobileNumber: String) : MobileInputEvent()
    object SendOtp : MobileInputEvent()
    object ClearError : MobileInputEvent()
    object ResetSendOtpResource : MobileInputEvent()
}

/**
 * ViewModel for Mobile Input screen
 */
class MobileInputViewModel(
    private val sendOtpUseCase: SendOtpUseCase,
    private val screenContentRepository: ScreenContentRepository
) : BaseViewModel<MobileInputUiState, MobileInputEvent>() {

    private val _sendOtpResource = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val sendOtpResource: StateFlow<Resource<Unit>> = _sendOtpResource.asStateFlow()

    override fun createInitialState(): MobileInputUiState {
        return MobileInputUiState()
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
            when (val result = screenContentRepository.getMobileInputScreenContent()) {
                is Result.Success -> {
                    val domainContent: MobileInputScreenContent = result.data
                    _uiState.value = _uiState.value.copy(
                        content = domainContent.toUiModel(),
                        isLoadingContent = false
                    )
                }
                is Result.Error -> {
                    // On error, use default content (fallback)
                    // In production, you might want to retry or show error
                    _uiState.value = _uiState.value.copy(
                        isLoadingContent = false
                        // content remains null, UI should handle this
                    )
                }
                is Result.Loading -> {
                    // Keep loading state
                    _uiState.value = _uiState.value.copy(isLoadingContent = true)
                }
            }
        }
    }

    override fun handleEvent(event: MobileInputEvent) {
        when (event) {
            is MobileInputEvent.MobileNumberChanged -> {
                updateMobileNumber(event.mobileNumber)
            }
            is MobileInputEvent.SendOtp -> {
                sendOtp()
            }
            is MobileInputEvent.ClearError -> {
                clearError()
            }
            is MobileInputEvent.ResetSendOtpResource -> {
                resetSendOtpResource()
            }
        }
    }
    
    private fun resetSendOtpResource() {
        _sendOtpResource.value = Resource.Loading()
    }

    private fun updateMobileNumber(mobileNumber: String) {
        // Remove all non-digit characters and limit to 10 digits
        val cleanedNumber = mobileNumber.filter { it.isDigit() }.take(10)
        
        // Validate using Indian mobile number regex: should start with 6-9 and be exactly 10 digits
        val indianMobileRegex = Regex("^[6-9][0-9]{9}$")
        val isValid = cleanedNumber.length == 10 && cleanedNumber.matches(indianMobileRegex)
        
        // Clear error when user starts typing (if number is not yet 10 digits or becomes valid)
        val shouldShowError = cleanedNumber.isNotEmpty() && 
                             cleanedNumber.length == 10 && 
                             !cleanedNumber.matches(indianMobileRegex)
        
        _uiState.value = _uiState.value.copy(
            mobileNumber = cleanedNumber,
            isMobileValid = isValid,
            error = if (shouldShowError) {
                _uiState.value.content?.errorMessages?.invalidMobileNumber 
                    ?: "Please enter a valid 10-digit mobile number"
            } else null
        )
    }

    private fun sendOtp() {
        val mobileNumber = _uiState.value.mobileNumber
        val content = _uiState.value
        
        // Validate mobile number using regex (should start with 6-9 and be exactly 10 digits)
        val indianMobileRegex = Regex("^[6-9][0-9]{9}$")
        if (mobileNumber.length != 10 || !mobileNumber.matches(indianMobileRegex)) {
            _uiState.value = _uiState.value.copy(
                error = content.content?.errorMessages?.invalidMobileNumber 
                    ?: "Please enter a valid 10-digit mobile number"
            )
            return
        }

        execute {
            _sendOtpResource.value = Resource.Loading()
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val result = sendOtpUseCase(mobileNumber)
            val content = _uiState.value.content
            _sendOtpResource.value = result.toResource().let { resource ->
                when (resource) {
                    is Resource.Success -> Resource.Success(Unit)
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
            
            if (_sendOtpResource.value !is Resource.Error) {
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
        _sendOtpResource.value = Resource.Error(errorMessage)
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = errorMessage
        )
    }
}

