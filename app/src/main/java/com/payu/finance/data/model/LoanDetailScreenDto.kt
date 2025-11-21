package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for Loan Detail Screen API Response
 */
data class LoanDetailScreenDto(
    @SerializedName("sections")
    val sections: LoanDetailSectionsDto,
    @SerializedName("type")
    val type: String
) {
    /**
     * Convert DTO to Domain model
     */
    fun toDomain(): com.payu.finance.domain.model.LoanDetailScreenContent {
        return com.payu.finance.domain.model.LoanDetailScreenContent(
            sections = sections.items.map { it.toDomain() }
        )
    }
}

/**
 * Sections DTO for Loan Detail
 */
data class LoanDetailSectionsDto(
    @SerializedName("items")
    val items: List<LoanDetailSectionItemDto>
)

/**
 * Section Item DTO for Loan Detail
 */
data class LoanDetailSectionItemDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("class")
    val className: String?,
    @SerializedName("meta")
    val meta: MetaDto?,
    @SerializedName("components")
    val components: ComponentsDto?,
    @SerializedName("actions")
    val actions: ActionsDto?,
    @SerializedName("assets")
    val assets: AssetsDto?
) {
    /**
     * Convert to Domain SectionItem
     * Inline conversion to avoid extension function ambiguity
     */
    fun toDomain(): com.payu.finance.domain.model.SectionItem {
        // Convert meta inline to avoid ambiguity with ProfileScreenDto extension functions
        val metaDomain = meta?.let {
            com.payu.finance.domain.model.Meta(
                background = it.background,
                loanDescription = it.loanDescription,
                percentage = it.percentage,
                label = it.label,
                color = it.color
            )
        }
        
        // Convert components inline
        val componentsDomain = components?.items?.map { componentItem ->
            com.payu.finance.domain.model.ComponentItem(
                type = componentItem.type,
                title = componentItem.title,
                subtitle = componentItem.subtitle,
                description = componentItem.description,
                className = componentItem.className,
                meta = componentItem.meta?.let { m ->
                    com.payu.finance.domain.model.Meta(
                        background = m.background,
                        loanDescription = m.loanDescription,
                        percentage = m.percentage,
                        label = m.label,
                        color = m.color
                    )
                },
                actions = componentItem.actions?.let { a ->
                    com.payu.finance.domain.model.ComponentActions(
                        default = a.default?.let { actionItem ->
                            com.payu.finance.domain.model.ActionItem(
                                text = actionItem.text,
                                type = actionItem.type,
                                url = actionItem.url
                            )
                        },
                        primary = a.primary?.let { actionItem ->
                            com.payu.finance.domain.model.ActionItem(
                                text = actionItem.text,
                                type = actionItem.type,
                                url = actionItem.url
                            )
                        }
                    )
                },
                assets = componentItem.assets?.let { assets ->
                    com.payu.finance.domain.model.ComponentAssets(
                        infoIcon = assets.infoIcon,
                        leadingIcon = assets.leadingIcon,
                        trailingIcon = assets.trailingIcon
                    )
                }
            )
        }
        
        // Convert actions inline
        val actionsDomain = actions?.let { a ->
            com.payu.finance.domain.model.ComponentActions(
                default = a.default?.let { actionItem ->
                    com.payu.finance.domain.model.ActionItem(
                        text = actionItem.text,
                        type = actionItem.type,
                        url = actionItem.url
                    )
                },
                primary = a.primary?.let { actionItem ->
                    com.payu.finance.domain.model.ActionItem(
                        text = actionItem.text,
                        type = actionItem.type,
                        url = actionItem.url
                    )
                }
            )
        }
        
        return com.payu.finance.domain.model.SectionItem(
            title = title,
            subtitle = subtitle,
            type = type,
            className = className,
            meta = metaDomain,
            components = componentsDomain,
            actions = actionsDomain
        )
    }
}

