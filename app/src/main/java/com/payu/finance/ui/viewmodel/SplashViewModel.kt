package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.common.toResource
import com.payu.finance.domain.usecase.AuthenticateUseCase
import com.payu.finance.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI State for Splash screen
 */
data class SplashUiState(
    val isLoading: Boolean = true,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

/**
 * UI Events for Splash screen
 */
sealed class SplashEvent {
    object CheckAuthentication : SplashEvent()
}

/**
 * ViewModel for Splash screen
 */
class SplashViewModel(
    private val authenticateUseCase: AuthenticateUseCase
) : BaseViewModel<SplashUiState, SplashEvent>() {
    
    private val _authResource = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val authResource: StateFlow<Resource<Boolean>> = _authResource.asStateFlow()
    
    override fun createInitialState(): SplashUiState {
        return SplashUiState()
    }
    
    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.CheckAuthentication -> {
                checkAuthentication()
            }
        }
    }
    
    init {
        checkAuthentication()
    }
    
    private fun checkAuthentication() {
        execute {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            _authResource.value = Resource.Loading()
            
            try {
                // Call authenticate API with stored cookies
                // CookieInterceptor will automatically add cookies if they exist
                // This will make a POST request to /users/authenticate with empty body {}
                val result = authenticateUseCase()
                val resource = result.toResource()
                
                when (resource) {
                    is Resource.Success -> {
                        // Check if authentication was successful
                        // API returns 200 OK with empty body {} if cookies are valid
                        val isAuthenticated = resource.data?.success != false // true if success is true or null (empty body)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = isAuthenticated
                        )
                        _authResource.value = Resource.Success(isAuthenticated)
                    }
                    is Resource.Error -> {
                        // Authentication failed - user needs to login
                        // This happens when cookies are invalid/expired or API returns error
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            error = resource.message
                        )
                        _authResource.value = Resource.Error(resource.message ?: "Authentication failed")
                    }
                    is Resource.Loading -> {
                        _authResource.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                // Handle any unexpected errors
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    error = e.message
                )
                _authResource.value = Resource.Error(e.message ?: "Authentication failed")
            }
        }
    }
    
    override fun handleError(exception: Exception) {
        _authResource.value = Resource.Error(exception.message ?: "Authentication failed")
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            isAuthenticated = false,
            error = exception.message ?: "Authentication failed"
        )
    }
}

