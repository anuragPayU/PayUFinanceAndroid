package com.payu.finance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.domain.usecase.GetLoanDetailScreenContentUseCase
import com.payu.finance.ui.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoanDetailViewModel(
    private val getLoanDetailScreenContentUseCase: GetLoanDetailScreenContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<LoanDetailUiState>>(Resource.Loading())
    val uiState: StateFlow<Resource<LoanDetailUiState>> = _uiState.asStateFlow()

    fun loadLoanDetails(loanId: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                when (val result = getLoanDetailScreenContentUseCase(loanId)) {
                    is Result.Success -> {
                        val screenContent = result.data
                        // Convert domain model to UI model
                        val uiState = mapDomainToUi(screenContent)
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

    /**
     * Map domain model to UI model
     */
    private fun mapDomainToUi(domain: com.payu.finance.domain.model.LoanDetailScreenContent): LoanDetailUiState {
        val sections = domain.sections.mapNotNull { section ->
            when (section.type) {
                "detail_card" -> {
                    LoanDetailSectionUiItem.DetailCard(
                        title = section.title ?: "",
                        subtitle = section.subtitle,
                        statusLabel = section.meta?.label,
                        statusColor = section.meta?.color
                    )
                }
                "emi_detail" -> {
                    val header = section.components?.firstOrNull { it.type == "emi_detail_header" }
                    val rows = section.components
                        ?.filter { it.type == "emi_detail_row" }
                        ?.map { component ->
                            EmiDetailRow(
                                title = component.title ?: "",
                                subtitle = component.subtitle ?: ""
                            )
                        } ?: emptyList()
                    
                    LoanDetailSectionUiItem.EmiDetail(
                        title = section.title ?: "",
                        header = header?.let {
                            EmiDetailHeader(
                                title = it.title ?: "",
                                subtitle = it.subtitle,
                                percentage = it.meta?.percentage
                            )
                        },
                        rows = rows,
                        primaryAction = section.actions?.primary?.let {
                            ActionItem(
                                text = it.text,
                                type = it.type,
                                url = it.url
                            )
                        }
                    )
                }
                "auto_pay_status" -> {
                    val statusCard = section.components?.firstOrNull { it.type == "non_actionable_card" }
                    LoanDetailSectionUiItem.AutoPayStatus(
                        title = section.title ?: "",
                        statusCard = AutoPayStatusCard(
                            title = statusCard?.title ?: "",
                            subtitle = statusCard?.subtitle
                        )
                    )
                }
                "forclosure_card" -> {
                    val foreclosureCard = section.components?.firstOrNull { it.type == "forclosure_card" }
                    LoanDetailSectionUiItem.ForeclosureCard(
                        title = section.title ?: "",
                        card = ForeclosureCardItem(
                            title = foreclosureCard?.title ?: "",
                            subtitle = foreclosureCard?.subtitle ?: "",
                            description = foreclosureCard?.description,
                            action = foreclosureCard?.actions?.default?.let {
                                ActionItem(
                                    text = it.text,
                                    type = it.type,
                                    url = it.url
                                )
                            }
                        )
                    )
                }
                "row_list_card" -> {
                    val items = section.components
                        ?.filter { it.type == "actionable_card" }
                        ?.map { component ->
                            ActionableCardItem(
                                title = component.title ?: "",
                                action = component.actions?.default?.let {
                                    ActionItem(
                                        text = it.text,
                                        type = it.type,
                                        url = it.url
                                    )
                                }
                            )
                        } ?: emptyList()
                    
                    LoanDetailSectionUiItem.RowListCard(
                        title = section.title ?: "",
                        items = items
                    )
                }
                else -> null
            }
        }
        
        return LoanDetailUiState(sections = sections)
    }
}

