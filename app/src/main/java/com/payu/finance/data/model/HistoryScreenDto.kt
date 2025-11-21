package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for History Screen API Response
 */
data class HistoryScreenDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("actions")
    val actions: HistoryActionsDto?,
    @SerializedName("sections")
    val sections: HistorySectionsDto,
    @SerializedName("type")
    val type: String
)

/**
 * History Actions DTO
 */
data class HistoryActionsDto(
    @SerializedName("primary")
    val primary: HistoryActionItemDto?
)

/**
 * History Action Item DTO
 */
data class HistoryActionItemDto(
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String?
)

/**
 * History Sections DTO
 */
data class HistorySectionsDto(
    @SerializedName("items")
    val items: List<HistorySectionItemDto>
)

/**
 * History Section Item DTO
 */
data class HistorySectionItemDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("class")
    val className: String?,
    @SerializedName("components")
    val components: HistoryComponentsDto?
)

/**
 * History Components DTO
 */
data class HistoryComponentsDto(
    @SerializedName("items")
    val items: List<HistoryComponentItemDto>
)

/**
 * History Component Item DTO
 */
data class HistoryComponentItemDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("meta")
    val meta: HistoryMetaDto?,
    @SerializedName("assets")
    val assets: HistoryAssetsDto?
)

/**
 * History Meta DTO
 */
data class HistoryMetaDto(
    @SerializedName("label")
    val label: String?,
    @SerializedName("color")
    val color: String?
)

/**
 * History Assets DTO
 */
data class HistoryAssetsDto(
    @SerializedName("leadingIcon")
    val leadingIcon: String?
)

/**
 * Mapper: DTO -> Domain
 */
fun HistoryScreenDto.toDomain(): com.payu.finance.domain.model.HistoryScreenContent {
    return com.payu.finance.domain.model.HistoryScreenContent(
        title = title,
        subtitle = subtitle,
        actions = actions?.toDomain(),
        sections = sections.items.map { it.toDomain() }
    )
}

fun HistoryActionsDto.toDomain(): com.payu.finance.domain.model.HistoryActions {
    return com.payu.finance.domain.model.HistoryActions(
        primary = primary?.toDomain()
    )
}

fun HistoryActionItemDto.toDomain(): com.payu.finance.domain.model.ActionItem {
    return com.payu.finance.domain.model.ActionItem(
        text = text,
        type = type,
        url = url
    )
}

fun HistorySectionItemDto.toDomain(): com.payu.finance.domain.model.HistorySectionItem {
    return com.payu.finance.domain.model.HistorySectionItem(
        type = type,
        className = className,
        components = components?.items?.map { it.toDomain() }
    )
}

fun HistoryComponentItemDto.toDomain(): com.payu.finance.domain.model.HistoryComponentItem {
    return com.payu.finance.domain.model.HistoryComponentItem(
        title = title,
        subtitle = subtitle,
        description = description,
        meta = meta?.toDomain(),
        assets = assets?.toDomain()
    )
}

fun HistoryMetaDto.toDomain(): com.payu.finance.domain.model.HistoryMeta {
    return com.payu.finance.domain.model.HistoryMeta(
        label = label,
        color = color
    )
}

fun HistoryAssetsDto.toDomain(): com.payu.finance.domain.model.HistoryAssets {
    return com.payu.finance.domain.model.HistoryAssets(
        leadingIcon = leadingIcon
    )
}

