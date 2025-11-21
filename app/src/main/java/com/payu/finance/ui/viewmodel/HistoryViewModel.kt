package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.toResource
import com.payu.finance.domain.usecase.GetHistoryScreenContentUseCase
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
    private val getHistoryScreenContentUseCase: GetHistoryScreenContentUseCase
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
            val result = getHistoryScreenContentUseCase().toResource()
            _historyResource.value = result.let { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val historyContent = resource.data
                        if (historyContent != null) {
                            // Map API response to UI state
                            val historyItems = mapHistoryContentToHistoryItems(historyContent)
                            Resource.Success(historyItems)
                        } else {
                            Resource.Error("No history data available")
                        }
                    }
                    is Resource.Error -> Resource.Error(resource.message ?: "Failed to load history")
                    is Resource.Loading -> Resource.Loading()
                }
            }
        }
    }

    /**
     * Maps HistoryScreenContent (Domain model) to List<HistoryItem> (UI model)
     */
    private fun mapHistoryContentToHistoryItems(historyContent: com.payu.finance.domain.model.HistoryScreenContent): List<HistoryItem> {
        val historyItems = mutableListOf<HistoryItem>()
        
        // Extract components from sections
        historyContent.sections.forEach { section ->
            section.components?.forEachIndexed { index, component ->
                historyItems.add(
                    HistoryItem(
                        id = "${section.type}_${index}",
                        title = component.title ?: "Repayment",
                        description = "", // Description field is empty in HistoryItem, date is used for date
                        amount = component.subtitle ?: "₹0", // subtitle contains the amount
                        date = component.description ?: "", // description contains the date/time
                        status = mapMetaToEmiStatus(component.meta),
                        loanId = "" // TODO: Extract loanId from component if available
                    )
                )
            }
        }
        
        return historyItems.sortedByDescending { it.date }
    }

    /**
     * Maps meta color/label to EmiStatus
     */
    private fun mapMetaToEmiStatus(meta: com.payu.finance.domain.model.HistoryMeta?): EmiStatus {
        if (meta == null) return EmiStatus.PENDING
        
        // Map based on color or label
        return when {
            meta.color?.contains("positive", ignoreCase = true) == true || 
            meta.color?.contains("postive", ignoreCase = true) == true ||
            meta.label?.equals("Successful", ignoreCase = true) == true -> EmiStatus.PAID
            
            meta.color?.contains("negative", ignoreCase = true) == true ||
            meta.color?.contains("error", ignoreCase = true) == true ||
            meta.label?.equals("Overdue", ignoreCase = true) == true -> EmiStatus.OVERDUE
            
            meta.color?.contains("warning", ignoreCase = true) == true ||
            meta.label?.equals("Pending", ignoreCase = true) == true -> EmiStatus.PENDING
            
            meta.label?.equals("Partial", ignoreCase = true) == true -> EmiStatus.PARTIALLY_PAID
            
            else -> EmiStatus.PENDING
        }
    }

    override fun handleError(exception: Exception) {
        _historyResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

