package com.payu.finance.domain.model

/**
 * Domain model for Profile Screen Content
 */
data class ProfileScreenContent(
    val screen: ProfileScreen,
    val subScreen: ProfileSubScreen?
)

/**
 * Profile Screen Domain Model
 */
data class ProfileScreen(
    val title: String,
    val subtitle: String,
    val description: String?,
    val meta: ProfileMeta?,
    val actions: ProfileActions?,
    val sections: ProfileSections,
    val type: String
)

/**
 * Profile SubScreen Domain Model
 */
data class ProfileSubScreen(
    val bottomSheet: ProfileBottomSheet?,
    val actions: ProfileActions?
)

/**
 * Profile BottomSheet Domain Model
 */
data class ProfileBottomSheet(
    val title: String,
    val subtitle: String
)

/**
 * Profile Sections Domain Model
 */
data class ProfileSections(
    val items: List<ProfileSectionItem>
)

/**
 * Profile Section Item Domain Model
 */
data class ProfileSectionItem(
    val type: String,
    val className: String?,
    val components: ProfileComponents?
)

/**
 * Profile Components Domain Model
 */
data class ProfileComponents(
    val items: List<ProfileComponentItem>
)

/**
 * Profile Component Item Domain Model
 */
data class ProfileComponentItem(
    val type: String,
    val title: String,
    val actions: ProfileActions?,
    val assets: ProfileAssets?
)

/**
 * Profile Actions Domain Model
 */
data class ProfileActions(
    val default: ProfileActionItem?,
    val primary: ProfileActionItem?,
    val secondary: ProfileActionItem?
)

/**
 * Profile Action Item Domain Model
 */
data class ProfileActionItem(
    val text: String?,
    val type: String,
    val url: String?
)

/**
 * Profile Assets Domain Model
 */
data class ProfileAssets(
    val leadingIcon: String?,
    val trailingIcon: String?
)

/**
 * Profile Meta Domain Model
 */
data class ProfileMeta(
    val appVersionLabel: String?
)

