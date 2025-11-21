package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.Result
import com.payu.finance.common.toResource
import com.payu.finance.domain.model.RepaymentStatus
import com.payu.finance.domain.usecase.GetLoansUseCase
import com.payu.finance.domain.usecase.GetRepaymentsUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI Events for Home screen
 */
sealed class HomeEvent {
    object LoadHomeData : HomeEvent()
    object Refresh : HomeEvent()
    data class PayRepayment(val repaymentId: String) : HomeEvent()
}

/**
 * ViewModel for Home screen
 */
class HomeViewModel(
    private val getLoansUseCase: GetLoansUseCase,
    private val getRepaymentsUseCase: GetRepaymentsUseCase,
    private val getHomeScreenContentUseCase: com.payu.finance.domain.usecase.GetHomeScreenContentUseCase
) : BaseViewModel<HomeUiState, HomeEvent>() {

    private val _homeResource = MutableStateFlow<Resource<HomeUiState>>(Resource.Loading())
    val homeResource: StateFlow<Resource<HomeUiState>> = _homeResource.asStateFlow()

    override fun createInitialState(): HomeUiState {
        return HomeUiState()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadHomeData -> loadHomeData()
            is HomeEvent.Refresh -> loadHomeData()
            is HomeEvent.PayRepayment -> handlePayRepayment(event.repaymentId)
        }
    }

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        execute {
            _homeResource.value = Resource.Loading()
            
            try {
                // Call Home API: GET /users/repayment-dashboard
                // CookieInterceptor will automatically add cookies
                val result = getHomeScreenContentUseCase()
                val homeContentResult = result.toResource()
                
                when (homeContentResult) {
                    is Resource.Success -> {
                        val homeContent = homeContentResult.data
                        if (homeContent != null) {
                            // Map API response to UI state
                            val homeState = mapHomeContentToUiState(homeContent)
                            _homeResource.value = Resource.Success(homeState)
                        } else {
                            // API returned null - show error
                            _homeResource.value = Resource.Error("No data received from API")
                        }
                    }
                    is Resource.Error -> {
                        // API call failed - show error instead of falling back
                        _homeResource.value = Resource.Error(
                            homeContentResult.message ?: "Failed to load home data"
                        )
                    }
                    is Resource.Loading -> {
                        _homeResource.value = Resource.Loading()
                    }
                }
            } catch (e: Exception) {
                // Handle any unexpected errors
                _homeResource.value = Resource.Error(e.message ?: "Failed to load home data")
            }
        }
    }
    
    private suspend fun loadHomeDataFromLoansAndRepayments() {
        val loansResult = getLoansUseCase().toResource()
        val repaymentsResult = getRepaymentsUseCase().toResource()

        when {
            loansResult is Resource.Error -> {
                _homeResource.value = Resource.Error(loansResult.message ?: "Failed to load loans")
            }
            repaymentsResult is Resource.Error -> {
                _homeResource.value = Resource.Error(repaymentsResult.message ?: "Failed to load repayments")
            }
            loansResult is Resource.Success && repaymentsResult is Resource.Success -> {
                val loans = loansResult.data ?: emptyList()
                val repayments = repaymentsResult.data ?: emptyList()
                
                val homeState = buildHomeState(loans, repayments)
                _homeResource.value = Resource.Success(homeState)
            }
            else -> {
                _homeResource.value = Resource.Loading()
            }
        }
    }
    
    private fun mapHomeContentToUiState(homeContent: com.payu.finance.domain.model.HomeScreenContent): HomeUiState {
        // Map API response sections to UI state
        var dueCard: DueCard? = null
        var emiProgress: EmiProgressCard? = null
        var nextRepayment: NextRepaymentCard? = null
        val allEmis = mutableListOf<EmiItem>()
        
        homeContent.sections.forEach { section ->
            when (section.type) {
                "header" -> {
                    section.components?.forEach { component ->
                        when (component.type) {
                            "due_repayment" -> {
                                // Map due repayment component
                                val amount = component.title?.replace("₹", "")?.replace(",", "")?.toDoubleOrNull() ?: 0.0
                                dueCard = DueCard(
                                    totalDueAmount = component.title ?: "₹0",
                                    overdueCount = 1, // TODO: Extract from component if available
                                    nextDueDate = null,
                                    isUrgent = true
                                )
                            }
                            "loan_detail" -> {
                                // Map loan detail component
                                val totalAmount = component.title?.replace("₹", "")?.replace(",", "")?.toDoubleOrNull() ?: 0.0
                                val percentage = component.meta?.percentage?.toFloatOrNull() ?: 0f
                                val paidAmount = totalAmount * (1 - percentage / 100)
                                val remainingAmount = totalAmount * (percentage / 100)
                                
                                emiProgress = EmiProgressCard(
                                    totalAmount = component.title ?: "₹0",
                                    paidAmount = formatCurrency(paidAmount),
                                    remainingAmount = formatCurrency(remainingAmount),
                                    progressPercentage = percentage / 100,
                                    totalInstallments = 5, // TODO: Extract from component if available
                                    paidInstallments = 1, // TODO: Extract from component if available
                                    remainingInstallments = 4 // TODO: Extract from component if available
                                )
                            }
                        }
                    }
                }
                "repayment_card" -> {
                    section.components?.forEach { component ->
                        if (component.type == "repayment_card") {
                            val amount = component.title?.replace("₹", "")?.replace(",", "")?.toDoubleOrNull() ?: 0.0
                            nextRepayment = NextRepaymentCard(
                                amount = component.title ?: "₹0",
                                dueDate = component.subtitle ?: "",
                                daysRemaining = null, // TODO: Calculate from date
                                isOverdue = false,
                                showRepaymentCta = true,
                                loanId = "", // TODO: Extract from component if available
                                repaymentId = component.actions?.default?.url ?: ""
                            )
                        }
                    }
                }
                "list" -> {
                    section.components?.forEach { component ->
                        if (component.type == "actionable_card_1") {
                            // Parse subtitle like "4/5 EMI Paid ⋅ ₹904/month"
                            val subtitle = component.subtitle ?: ""
                            val status = when (component.meta?.label) {
                                "Active" -> EmiStatus.PENDING
                                else -> EmiStatus.PENDING
                            }
                            
                            val url = component.actions?.default?.url ?: ""
                            val loanId = extractLoanIdFromUrl(url)
                            
                            allEmis.add(
                                EmiItem(
                                    id = url,
                                    installmentNumber = subtitle.split("⋅")[0].trim(),
                                    amount = component.title ?: "₹0",
                                    dueDate = "",
                                    status = status,
                                    loanId = loanId
                                )
                            )
                        }
                    }
                }
            }
        }
        
        return HomeUiState(
            emiProgress = emiProgress,
            nextRepayment = nextRepayment,
            dueCard = dueCard,
            allEmis = allEmis,
            isLoading = false,
            error = null
        )
    }

    private fun buildHomeState(
        loans: List<com.payu.finance.domain.model.Loan>,
        repayments: List<com.payu.finance.domain.model.Repayment>
    ): HomeUiState {
        val activeLoans = loans.filter { it.status == com.payu.finance.domain.model.LoanStatus.ACTIVE }
        
        // Build EMI Progress Card
        val emiProgress = activeLoans.firstOrNull()?.let { loan ->
            val totalPaid = loan.amount - loan.remainingAmount
            val progress = if (loan.amount > 0) (totalPaid / loan.amount).toFloat() else 0f
            val paidInstallments = repayments.count { 
                it.loanId == loan.id && it.status == RepaymentStatus.PAID 
            }
            val totalInstallments = repayments.count { it.loanId == loan.id }
            
            EmiProgressCard(
                totalAmount = formatCurrency(loan.amount),
                paidAmount = formatCurrency(totalPaid),
                remainingAmount = formatCurrency(loan.remainingAmount),
                progressPercentage = progress,
                totalInstallments = totalInstallments,
                paidInstallments = paidInstallments,
                remainingInstallments = totalInstallments - paidInstallments
            )
        }

        // Build Next Repayment Card
        val nextRepayment = repayments
            .filter { it.status == RepaymentStatus.PENDING || it.status == RepaymentStatus.OVERDUE }
            .minByOrNull { it.dueDate }
            ?.let { repayment ->
                val isOverdue = repayment.status == RepaymentStatus.OVERDUE
                NextRepaymentCard(
                    amount = formatCurrency(repayment.amount),
                    dueDate = formatDate(repayment.dueDate),
                    daysRemaining = calculateDaysRemaining(repayment.dueDate),
                    isOverdue = isOverdue,
                    showRepaymentCta = true, // Can be controlled based on business logic
                    loanId = repayment.loanId,
                    repaymentId = repayment.id
                )
            }

        // Build Due Card (if there are overdue repayments)
        val overdueRepayments = repayments.filter { it.status == RepaymentStatus.OVERDUE }
        val dueCard = if (overdueRepayments.isNotEmpty()) {
            val totalDue = overdueRepayments.sumOf { it.amount }
            val nextDue = overdueRepayments.minByOrNull { it.dueDate }
            DueCard(
                totalDueAmount = formatCurrency(totalDue),
                overdueCount = overdueRepayments.size,
                nextDueDate = nextDue?.dueDate?.let { formatDate(it) },
                isUrgent = overdueRepayments.any { calculateDaysOverdue(it.dueDate) > 7 }
            )
        } else null

        // Build All EMIs List
        val allEmis = repayments.map { repayment ->
            EmiItem(
                id = repayment.id,
                installmentNumber = "#${repayment.installmentNumber}",
                amount = formatCurrency(repayment.amount),
                dueDate = formatDate(repayment.dueDate),
                status = when (repayment.status) {
                    RepaymentStatus.PAID -> EmiStatus.PAID
                    RepaymentStatus.PENDING -> EmiStatus.PENDING
                    RepaymentStatus.OVERDUE -> EmiStatus.OVERDUE
                    RepaymentStatus.PARTIALLY_PAID -> EmiStatus.PARTIALLY_PAID
                },
                loanId = repayment.loanId
            )
        }.sortedByDescending { it.dueDate }

        return HomeUiState(
            emiProgress = emiProgress,
            nextRepayment = nextRepayment,
            dueCard = dueCard,
            allEmis = allEmis,
            isLoading = false,
            error = null
        )
    }

    private fun handlePayRepayment(repaymentId: String) {
        // TODO: Implement repayment payment logic
        execute {
            // Navigate to payment screen or process payment
        }
    }

    private fun formatCurrency(amount: Double): String {
        return "₹${String.format("%.2f", amount)}"
    }

    private fun formatDate(dateString: String): String {
        // TODO: Implement proper date formatting
        return dateString
    }

    private fun calculateDaysRemaining(dueDate: String): Int? {
        // TODO: Implement date calculation
        return null
    }

    private fun calculateDaysOverdue(dueDate: String): Int {
        // TODO: Implement date calculation
        return 0
    }

    /**
     * Extracts loan ID from a URL path.
     * Handles formats like:
     * - "/loan-details/2103694" -> "2103694"
     * - "/loan-details/2103694/" -> "2103694"
     * - "loan-details/2103694" -> "2103694"
     * - "2103694" -> "2103694"
     */
    private fun extractLoanIdFromUrl(url: String): String {
        if (url.isBlank()) return ""
        
        // Remove leading/trailing slashes and split by '/'
        val parts = url.trim().trim('/').split('/')
        
        // Find the last numeric part (loan ID)
        // Common patterns: /loan-details/{id}, /loans/{id}, etc.
        val loanId = parts.lastOrNull() ?: ""
        
        // If the last part is empty or not numeric, try to find a numeric part
        return if (loanId.isNotBlank() && loanId.all { it.isDigit() }) {
            loanId
        } else {
            // Fallback: find any numeric part in the URL
            parts.findLast { it.isNotBlank() && it.all { char -> char.isDigit() } } ?: url
        }
    }

    override fun handleError(exception: Exception) {
        _homeResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

