package com.payu.finance.data.model

import com.google.gson.annotations.SerializedName

/**
 * DTO for Profile Screen API Response
 */
data class ProfileScreenDto(
    @SerializedName("screen")
    val screen: ScreenDto,
    @SerializedName("subScreen")
    val subScreen: SubScreenDto?
)

/**
 * Screen DTO
 */
data class ScreenDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("meta")
    val meta: ProfileMetaDto?,
    @SerializedName("actions")
    val actions: ProfileActionsDto?,
    @SerializedName("sections")
    val sections: ProfileSectionsDto,
    @SerializedName("type")
    val type: String
)

/**
 * SubScreen DTO (for bottom sheet/dialog)
 */
data class SubScreenDto(
    @SerializedName("bottomSheet")
    val bottomSheet: BottomSheetDto?,
    @SerializedName("actions")
    val actions: ProfileActionsDto?
)

/**
 * BottomSheet DTO
 */
data class BottomSheetDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String
)

/**
 * Profile Sections DTO
 */
data class ProfileSectionsDto(
    @SerializedName("items")
    val items: List<ProfileSectionItemDto>
)

/**
 * Profile Section Item DTO
 */
data class ProfileSectionItemDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("class")
    val className: String?,
    @SerializedName("components")
    val components: ProfileComponentsDto?
)

/**
 * Profile Components DTO
 */
data class ProfileComponentsDto(
    @SerializedName("items")
    val items: List<ProfileComponentItemDto>
)

/**
 * Profile Component Item DTO
 */
data class ProfileComponentItemDto(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("actions")
    val actions: ProfileActionsDto?,
    @SerializedName("assets")
    val assets: ProfileAssetsDto?
)

/**
 * Profile Actions DTO
 */
data class ProfileActionsDto(
    @SerializedName("default")
    val default: ProfileActionItemDto? = null,
    @SerializedName("primary")
    val primary: ProfileActionItemDto? = null,
    @SerializedName("secondary")
    val secondary: ProfileActionItemDto? = null
)

/**
 * Profile Action Item DTO
 */
data class ProfileActionItemDto(
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String?
)

/**
 * Profile Assets DTO
 */
data class ProfileAssetsDto(
    @SerializedName("leadingIcon")
    val leadingIcon: String?,
    @SerializedName("trailingIcon")
    val trailingIcon: String?
)

/**
 * Profile Meta DTO
 */
data class ProfileMetaDto(
    @SerializedName("appVersionLabel")
    val appVersionLabel: String?
)

/**
 * Mapper: DTO -> Domain
 */
fun ProfileScreenDto.toDomain(): com.payu.finance.domain.model.ProfileScreenContent {
    return com.payu.finance.domain.model.ProfileScreenContent(
        screen = screen.toDomain(),
        subScreen = subScreen?.toDomain()
    )
}

fun ScreenDto.toDomain(): com.payu.finance.domain.model.ProfileScreen {
    return com.payu.finance.domain.model.ProfileScreen(
        title = title,
        subtitle = subtitle,
        description = description,
        meta = meta?.toDomain(),
        actions = actions?.toDomain(),
        sections = sections.toDomain(),
        type = type
    )
}

fun SubScreenDto.toDomain(): com.payu.finance.domain.model.ProfileSubScreen {
    return com.payu.finance.domain.model.ProfileSubScreen(
        bottomSheet = bottomSheet?.toDomain(),
        actions = actions?.toDomain()
    )
}

fun BottomSheetDto.toDomain(): com.payu.finance.domain.model.ProfileBottomSheet {
    return com.payu.finance.domain.model.ProfileBottomSheet(
        title = title,
        subtitle = subtitle
    )
}

fun ProfileSectionsDto.toDomain(): com.payu.finance.domain.model.ProfileSections {
    return com.payu.finance.domain.model.ProfileSections(
        items = items.map { it.toDomain() }
    )
}

fun ProfileSectionItemDto.toDomain(): com.payu.finance.domain.model.ProfileSectionItem {
    return com.payu.finance.domain.model.ProfileSectionItem(
        type = type,
        className = className,
        components = components?.toDomain()
    )
}

fun ProfileComponentsDto.toDomain(): com.payu.finance.domain.model.ProfileComponents {
    return com.payu.finance.domain.model.ProfileComponents(
        items = items.map { it.toDomain() }
    )
}

fun ProfileComponentItemDto.toDomain(): com.payu.finance.domain.model.ProfileComponentItem {
    return com.payu.finance.domain.model.ProfileComponentItem(
        type = type,
        title = title,
        actions = actions?.toDomain(),
        assets = assets?.toDomain()
    )
}

fun ProfileActionsDto.toDomain(): com.payu.finance.domain.model.ProfileActions {
    return com.payu.finance.domain.model.ProfileActions(
        default = default?.toDomain(),
        primary = primary?.toDomain(),
        secondary = secondary?.toDomain()
    )
}

fun ProfileActionItemDto.toDomain(): com.payu.finance.domain.model.ProfileActionItem {
    return com.payu.finance.domain.model.ProfileActionItem(
        text = text,
        type = type,
        url = url
    )
}

fun ProfileAssetsDto.toDomain(): com.payu.finance.domain.model.ProfileAssets {
    return com.payu.finance.domain.model.ProfileAssets(
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon
    )
}

fun ProfileMetaDto.toDomain(): com.payu.finance.domain.model.ProfileMeta {
    return com.payu.finance.domain.model.ProfileMeta(
        appVersionLabel = appVersionLabel
    )
}

