package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for Home Screen API Response
 */
data class HomeScreenDto(
    @SerializedName("sections")
    val sections: SectionsDto,
    @SerializedName("assets")
    val assets: AssetsDto?,
    @SerializedName("actions")
    val actions: ActionsDto?,
    @SerializedName("type")
    val type: String
)

/**
 * Sections DTO
 */
data class SectionsDto(
    @SerializedName("items")
    val items: List<SectionItemDto>
)

/**
 * Section Item DTO
 */
data class SectionItemDto(
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("class")
    val className: String?,
    @SerializedName("meta")
    val meta: MetaDto?,
    @SerializedName("components")
    val components: ComponentsDto?,
    @SerializedName("actions")
    val actions: ActionsDto? = null
)

/**
 * Components DTO
 */
data class ComponentsDto(
    @SerializedName("items")
    val items: List<ComponentItemDto>
)

/**
 * Component Item DTO
 */
data class ComponentItemDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("subtitle")
    val subtitle: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("class")
    val className: String?,
    @SerializedName("meta")
    val meta: MetaDto?,
    @SerializedName("actions")
    val actions: ActionsDto?,
    @SerializedName("assets")
    val assets: AssetsDto?
)

/**
 * Meta DTO
 */
data class MetaDto(
    @SerializedName("background")
    val background: String?,
    @SerializedName("loanDescription")
    val loanDescription: String?,
    @SerializedName("percentage")
    val percentage: String?,
    @SerializedName("label")
    val label: String?,
    @SerializedName("color")
    val color: String?
)

/**
 * Actions DTO - Can be used for both component actions and root navigation actions
 */
data class ActionsDto(
    @SerializedName("default")
    val default: ActionItemDto? = null,
    @SerializedName("primary")
    val primary: ActionItemDto? = null,
    @SerializedName("home")
    val home: ActionItemDto? = null,
    @SerializedName("history")
    val history: ActionItemDto? = null,
    @SerializedName("profile")
    val profile: ActionItemDto? = null
)

/**
 * Action Item DTO
 */
data class ActionItemDto(
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String?
)

/**
 * Assets DTO
 */
data class AssetsDto(
    @SerializedName("home")
    val home: String?,
    @SerializedName("history")
    val history: String?,
    @SerializedName("profile")
    val profile: String?,
    @SerializedName("infoIcon")
    val infoIcon: String?,
    @SerializedName("leadingIcon")
    val leadingIcon: String?,
    @SerializedName("trailingIcon")
    val trailingIcon: String?
)

/**
 * Mapper: DTO -> Domain
 */
fun HomeScreenDto.toDomain(): com.payu.finance.domain.model.HomeScreenContent {
    return com.payu.finance.domain.model.HomeScreenContent(
        sections = sections.items.map { it.toDomain() },
        assets = assets?.toDomain(),
        actions = actions?.toNavigationActions()
    )
}

fun SectionItemDto.toDomain(): com.payu.finance.domain.model.SectionItem {
    return com.payu.finance.domain.model.SectionItem(
        title = title,
        subtitle = subtitle,
        type = type,
        className = className,
        meta = meta?.toDomain(),
        components = components?.items?.map { it.toDomain() },
        actions = actions?.toComponentActions()
    )
}

fun ComponentItemDto.toDomain(): com.payu.finance.domain.model.ComponentItem {
    return com.payu.finance.domain.model.ComponentItem(
        type = type,
        title = title,
        subtitle = subtitle,
        description = description,
        className = className,
        meta = meta?.toDomain(),
        actions = actions?.toComponentActions(),
        assets = assets?.toComponentAssets()
    )
}

fun MetaDto.toDomain(): com.payu.finance.domain.model.Meta {
    return com.payu.finance.domain.model.Meta(
        background = background,
        loanDescription = loanDescription,
        percentage = percentage,
        label = label,
        color = color
    )
}

fun ActionsDto.toNavigationActions(): com.payu.finance.domain.model.NavigationActions {
    return com.payu.finance.domain.model.NavigationActions(
        home = home?.toDomain(),
        history = history?.toDomain(),
        profile = profile?.toDomain()
    )
}

fun ActionsDto.toComponentActions(): com.payu.finance.domain.model.ComponentActions {
    return com.payu.finance.domain.model.ComponentActions(
        default = default?.toDomain(),
        primary = primary?.toDomain()
    )
}

fun ActionItemDto.toDomain(): com.payu.finance.domain.model.ActionItem {
    return com.payu.finance.domain.model.ActionItem(
        text = text,
        type = type,
        url = url
    )
}

fun AssetsDto.toDomain(): com.payu.finance.domain.model.Assets {
    return com.payu.finance.domain.model.Assets(
        home = home,
        history = history,
        profile = profile
    )
}

fun AssetsDto.toComponentAssets(): com.payu.finance.domain.model.ComponentAssets {
    return com.payu.finance.domain.model.ComponentAssets(
        infoIcon = infoIcon,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

fun HomeScreenDto.toDomainWithActions(): com.payu.finance.domain.model.HomeScreenContent {
    return this.toDomain()
}

