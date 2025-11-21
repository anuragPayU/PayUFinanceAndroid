package com.payu.finance.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel class that provides common functionality
 * All ViewModels should extend this class
 */
abstract class BaseViewModel<UiState, UiEvent> : ViewModel() {

    protected val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /**
     * Create initial UI state
     */
    protected abstract fun createInitialState(): UiState

    /**
     * Handle UI events
     */
    abstract fun handleEvent(event: UiEvent)

    /**
     * Execute a coroutine in ViewModel scope
     */
    protected fun execute(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    /**
     * Handle errors - override in child classes for custom error handling
     */
    protected open fun handleError(exception: Exception) {
        // Log error or update UI state with error
    }
}

