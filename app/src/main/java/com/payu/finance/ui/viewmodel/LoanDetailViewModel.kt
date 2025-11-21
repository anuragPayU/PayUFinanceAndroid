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
import kotlinx.coroutines.delay

class LoanDetailViewModel(
    private val getLoanDetailScreenContentUseCase: GetLoanDetailScreenContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<Resource<LoanDetailUiState>>(Resource.Loading())
    val uiState: StateFlow<Resource<LoanDetailUiState>> = _uiState.asStateFlow()

    fun loadLoanDetails(loanId: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading()
            try {
                // Simulate network delay
                delay(500)
                
                // Return dummy data instead of calling API
                val uiState = createDummyLoanDetailData(loanId)
                _uiState.value = Resource.Success(uiState)
            } catch (e: Exception) {
                _uiState.value = Resource.Error(e.message ?: "Failed to load loan details")
            }
        }
    }

    /**
     * Create dummy loan detail data for testing/development
     */
    private fun createDummyLoanDetailData(loanId: String): LoanDetailUiState {
        return LoanDetailUiState(
            sections = listOf(
                // Detail Card Section
                LoanDetailSectionUiItem.DetailCard(
                    title = "₹1,00,000",
                    subtitle = "Total Loan Amount",
                    statusLabel = "Active",
                    statusColor = "positive"
                ),
                
                // EMI Detail Section
                LoanDetailSectionUiItem.EmiDetail(
                    title = "EMI Details",
                    header = EmiDetailHeader(
                        title = "₹8,333",
                        subtitle = "Monthly EMI",
                        percentage = "40"
                    ),
                    rows = listOf(
                        EmiDetailRow(
                            title = "Total Amount",
                            subtitle = "₹1,00,000"
                        ),
                        EmiDetailRow(
                            title = "Paid Amount",
                            subtitle = "₹40,000"
                        ),
                        EmiDetailRow(
                            title = "Remaining Amount",
                            subtitle = "₹60,000"
                        ),
                        EmiDetailRow(
                            title = "Installments Paid",
                            subtitle = "5/12"
                        ),
                        EmiDetailRow(
                            title = "Next Due Date",
                            subtitle = "15 Jan 2024"
                        )
                    ),
                    primaryAction = ActionItem(
                        text = "View Schedule",
                        type = "SEE_SECHEDULE",
                        url = null
                    )
                ),
                
                // Auto Pay Status Section
                LoanDetailSectionUiItem.AutoPayStatus(
                    title = "Auto Pay",
                    statusCard = AutoPayStatusCard(
                        title = "Auto Pay Enabled",
                        subtitle = "Your EMIs will be automatically deducted on the due date"
                    )
                ),
                
                // Foreclosure Card Section
                LoanDetailSectionUiItem.ForeclosureCard(
                    title = "Foreclosure",
                    card = ForeclosureCardItem(
                        title = "Foreclose Loan",
                        subtitle = "Pay off your remaining balance early",
                        description = "You can foreclose your loan by paying the remaining balance. Early foreclosure may have applicable charges.",
                        action = ActionItem(
                            text = "Foreclose Now",
                            type = "FORECLOSURE",
                            url = null
                        )
                    )
                ),
                
                // Row List Card Section (Documents)
                LoanDetailSectionUiItem.RowListCard(
                    title = "Documents",
                    items = listOf(
                        ActionableCardItem(
                            title = "Loan Agreement",
                            action = ActionItem(
                                text = "Download",
                                type = "DOWNLOAD",
                                url = "https://example.com/documents/loan-agreement.pdf"
                            )
                        ),
                        ActionableCardItem(
                            title = "Payment Receipt",
                            action = ActionItem(
                                text = "Download",
                                type = "DOWNLOAD",
                                url = "https://example.com/documents/payment-receipt.pdf"
                            )
                        ),
                        ActionableCardItem(
                            title = "Statement",
                            action = ActionItem(
                                text = "Download",
                                type = "DOWNLOAD",
                                url = "https://example.com/documents/statement.pdf"
                            )
                        )
                    )
                )
            )
        )
    }

    /**
     * Map domain model to UI model (kept for future use when API is ready)
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

