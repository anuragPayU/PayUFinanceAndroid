package com.payu.finance.data.datasource

import com.payu.finance.data.api.ScreenContentApiService
import com.payu.finance.data.model.ActionItemDto
import com.payu.finance.data.model.ActionsDto
import com.payu.finance.data.model.AssetsDto
import com.payu.finance.data.model.ComponentItemDto
import com.payu.finance.data.model.ComponentsDto
import com.payu.finance.data.model.HomeScreenDto
import com.payu.finance.data.model.MetaDto
import com.payu.finance.data.model.SectionItemDto
import com.payu.finance.data.model.SectionsDto
import kotlinx.coroutines.delay

/**
 * Remote data source for Home Screen
 * Handles API calls and error handling
 * Currently returns mock data - replace with actual API calls when backend is ready
 */
class HomeRemoteDataSource(
    private val screenContentApiService: ScreenContentApiService
) {
    /**
     * Get Home Screen Content
     * TODO: Replace with actual API call when backend is ready
     */
    suspend fun getHomeScreenContent(): HomeScreenDto {
        // Simulate network delay
        delay(500)
        
        // Return mock data based on the provided JSON structure
        return HomeScreenDto(
            sections = SectionsDto(
                items = listOf(
                    // Header Section
                    SectionItemDto(
                        title = "Hi, Rahul",
                        subtitle = "Track all your loans",
                        type = "header",
                        className = "section",
                        meta = MetaDto(
                            background = "image/gradient value",
                            loanDescription = null,
                            percentage = null,
                            label = null,
                            color = null
                        ),
                        components = ComponentsDto(
                            items = listOf(
                                // Due Repayment Component
                                ComponentItemDto(
                                    type = "due_repayment",
                                    title = "₹1,500 amount due",
                                    subtitle = "Pay now to avoid charges",
                                    description = null,
                                    className = null,
                                    meta = null,
                                    actions = ActionsDto(
                                        default = ActionItemDto(
                                            text = "Pay now",
                                            type = "WEB_LINK",
                                            url = "web link"
                                        ),
                                        primary = null
                                    ),
                                    assets = AssetsDto(
                                        home = null,
                                        history = null,
                                        profile = null,
                                        infoIcon = "",
                                        leadingIcon = "",
                                        trailingIcon = ""
                                    )
                                ),
                                // Loan Detail Component
                                ComponentItemDto(
                                    type = "loan_detail",
                                    title = "₹84,345",
                                    subtitle = "Total Outstanding amount left",
                                    description = "80% Left to pay",
                                    className = "component",
                                    meta = MetaDto(
                                        background = null,
                                        loanDescription = "out of 84,345",
                                        percentage = "80",
                                        label = null,
                                        color = null
                                    ),
                                    actions = null,
                                    assets = null
                                )
                            )
                        )
                    ),
                    // Repayment Card Section
                    SectionItemDto(
                        title = "Upcoming repayment",
                        subtitle = null,
                        type = "repayment_card",
                        className = null,
                        meta = null,
                        components = ComponentsDto(
                            items = listOf(
                                ComponentItemDto(
                                    type = "repayment_card",
                                    title = "₹12,345",
                                    subtitle = "Due on 20 Dec' 2025",
                                    description = null,
                                    className = null,
                                    meta = null,
                                    actions = ActionsDto(
                                        default = ActionItemDto(
                                            text = "Pay now",
                                            type = "REPAYMENT",
                                            url = "url for repayment"
                                        ),
                                        primary = null
                                    ),
                                    assets = null
                                )
                            )
                        )
                    ),
                    // EMIs List Section
                    SectionItemDto(
                        title = "EMIs",
                        subtitle = null,
                        type = "list",
                        className = null,
                        meta = null,
                        components = ComponentsDto(
                            items = listOf(
                                ComponentItemDto(
                                    type = "actionable_card_1",
                                    title = "₹4,00,000",
                                    subtitle = "4/5 EMI Paid ⋅ ₹904/month",
                                    description = null,
                                    className = null,
                                    meta = MetaDto(
                                        background = null,
                                        loanDescription = null,
                                        percentage = null,
                                        label = "Active",
                                        color = "#10B981"
                                    ),
                                    actions = ActionsDto(
                                        default = ActionItemDto(
                                            text = "",
                                            type = "REPAYMENT_DETAIL",
                                            url = "url to show detail of repayment"
                                        ),
                                        primary = null
                                    ),
                                    assets = AssetsDto(
                                        home = null,
                                        history = null,
                                        profile = null,
                                        infoIcon = null,
                                        leadingIcon = "",
                                        trailingIcon = ""
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            assets = AssetsDto(
                home = "",
                history = "",
                profile = "",
                infoIcon = null,
                leadingIcon = null,
                trailingIcon = null
            ),
            actions = ActionsDto(
                default = null,
                primary = null,
                home = ActionItemDto(
                    text = "Home",
                    type = "HOME",
                    url = ""
                ),
                history = ActionItemDto(
                    text = "History",
                    type = "HISTORY",
                    url = ""
                ),
                profile = ActionItemDto(
                    text = "Profile",
                    type = "PROFILE",
                    url = ""
                )
            ),
            type = "screen"
        )
        
        // Uncomment below when backend is ready:
        /*
        val response = screenContentApiService.getHomeScreenContent()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Failed to fetch home screen: ${response.message()}")
        }
        */
    }
}

