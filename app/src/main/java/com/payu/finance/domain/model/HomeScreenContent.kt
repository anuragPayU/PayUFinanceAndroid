package com.payu.finance.domain.model

/**
 * Domain model for Home Screen Content
 */
data class HomeScreenContent(
    val sections: List<SectionItem>,
    val assets: Assets?,
    val actions: NavigationActions?
)

/**
 * Section Item
 */
data class SectionItem(
    val title: String?,
    val subtitle: String?,
    val type: String,
    val className: String?,
    val meta: Meta?,
    val components: List<ComponentItem>?,
    val actions: ComponentActions? = null
)

/**
 * Component Item
 */
data class ComponentItem(
    val type: String,
    val title: String?,
    val subtitle: String?,
    val description: String?,
    val className: String?,
    val meta: Meta?,
    val actions: ComponentActions?,
    val assets: ComponentAssets?
)

/**
 * Meta
 */
data class Meta(
    val background: String?,
    val loanDescription: String?,
    val percentage: String?,
    val label: String?,
    val color: String?
)

/**
 * Component Actions
 */
data class ComponentActions(
    val default: ActionItem?,
    val primary: ActionItem?
)

/**
 * Action Item
 */
data class ActionItem(
    val text: String?,
    val type: String,
    val url: String?
)

/**
 * Assets
 */
data class Assets(
    val home: String?,
    val history: String?,
    val profile: String?
)

/**
 * Component Assets
 */
data class ComponentAssets(
    val infoIcon: String?,
    val leadingIcon: String?,
    val trailingIcon: String?
)

/**
 * Navigation Actions
 */
data class NavigationActions(
    val home: ActionItem?,
    val history: ActionItem?,
    val profile: ActionItem?
)

