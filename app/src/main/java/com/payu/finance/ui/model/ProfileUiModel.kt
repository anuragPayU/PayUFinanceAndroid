package com.payu.finance.ui.model

/**
 * UI State for Profile Screen
 */
data class ProfileUiState(
    val userName: String = "",
    val userPhone: String = "",
    val userDescription: String? = null,
    val appVersionLabel: String? = null,
    val menuItems: List<ProfileMenuItem> = emptyList(),
    val logoutBottomSheet: LogoutBottomSheet? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Profile Menu Item
 */
data class ProfileMenuItem(
    val title: String,
    val actionType: String,
    val actionUrl: String?,
    val leadingIcon: String?,
    val trailingIcon: String?
)

/**
 * Logout Bottom Sheet Data
 */
data class LogoutBottomSheet(
    val title: String,
    val subtitle: String,
    val primaryActionText: String,
    val secondaryActionText: String
)

