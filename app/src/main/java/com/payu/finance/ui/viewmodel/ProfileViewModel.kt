package com.payu.finance.ui.viewmodel

import com.payu.finance.common.Resource
import com.payu.finance.common.toResource
import com.payu.finance.domain.usecase.GetProfileScreenContentUseCase
import com.payu.finance.ui.base.BaseViewModel
import com.payu.finance.ui.model.ProfileMenuItem
import com.payu.finance.ui.model.ProfileUiState
import com.payu.finance.ui.model.LogoutBottomSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * UI Events for Profile screen
 */
sealed class ProfileEvent {
    object LoadProfileData : ProfileEvent()
    object Refresh : ProfileEvent()
    object ShowLogoutBottomSheet : ProfileEvent()
    object DismissLogoutBottomSheet : ProfileEvent()
    object ConfirmLogout : ProfileEvent()
}

/**
 * ViewModel for Profile screen
 */
class ProfileViewModel(
    private val getProfileScreenContentUseCase: GetProfileScreenContentUseCase
) : BaseViewModel<ProfileUiState, ProfileEvent>() {

    private val _profileResource = MutableStateFlow<Resource<ProfileUiState>>(Resource.Loading())
    val profileResource: StateFlow<Resource<ProfileUiState>> = _profileResource.asStateFlow()

    private val _showLogoutBottomSheet = MutableStateFlow(false)
    val showLogoutBottomSheet: StateFlow<Boolean> = _showLogoutBottomSheet.asStateFlow()

    override fun createInitialState(): ProfileUiState {
        return ProfileUiState()
    }

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadProfileData -> loadProfileData()
            is ProfileEvent.Refresh -> loadProfileData()
            is ProfileEvent.ShowLogoutBottomSheet -> _showLogoutBottomSheet.value = true
            is ProfileEvent.DismissLogoutBottomSheet -> _showLogoutBottomSheet.value = false
            is ProfileEvent.ConfirmLogout -> handleLogout()
        }
    }

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        execute {
            _profileResource.value = Resource.Loading()

            val profileContentResult = getProfileScreenContentUseCase().toResource()

            when (profileContentResult) {
                is Resource.Success -> {
                    val profileContent = profileContentResult.data
                    if (profileContent != null) {
                        // Map API response to UI state
                        val profileState = mapProfileContentToUiState(profileContent)
                        _profileResource.value = Resource.Success(profileState)
                    } else {
                        _profileResource.value = Resource.Error("Failed to load profile data")
                    }
                }
                is Resource.Error -> {
                    _profileResource.value = Resource.Error(profileContentResult.message ?: "Failed to load profile data")
                }
                is Resource.Loading -> {
                    _profileResource.value = Resource.Loading()
                }
            }
        }
    }

    private fun mapProfileContentToUiState(profileContent: com.payu.finance.domain.model.ProfileScreenContent): ProfileUiState {
        val screen = profileContent.screen
        val subScreen = profileContent.subScreen

        // Map menu items from sections
        // Group all sections into a single list of menu items
        val menuItems = mutableListOf<ProfileMenuItem>()
        screen.sections.items.forEach { section ->
            section.components?.items?.forEach { component ->
                menuItems.add(
                    ProfileMenuItem(
                        title = component.title,
                        actionType = component.actions?.default?.type ?: "",
                        actionUrl = component.actions?.default?.url,
                        leadingIcon = component.assets?.leadingIcon,
                        trailingIcon = component.assets?.trailingIcon
                    )
                )
            }
        }

        // Map logout bottom sheet
        val logoutBottomSheet = subScreen?.bottomSheet?.let { bottomSheet ->
            LogoutBottomSheet(
                title = bottomSheet.title,
                subtitle = bottomSheet.subtitle,
                primaryActionText = subScreen.actions?.primary?.text ?: "Logout",
                secondaryActionText = subScreen.actions?.secondary?.text ?: "Stay"
            )
        }

        return ProfileUiState(
            userName = screen.title,
            userPhone = screen.subtitle,
            userDescription = screen.description,
            appVersionLabel = screen.meta?.appVersionLabel,
            menuItems = menuItems,
            logoutBottomSheet = logoutBottomSheet,
            isLoading = false,
            error = null
        )
    }

    private fun handleLogout() {
        execute {
            // TODO: Implement actual logout logic
            _showLogoutBottomSheet.value = false
            // Navigate to login screen or clear session
        }
    }

    override fun handleError(exception: Exception) {
        _profileResource.value = Resource.Error(exception.message ?: "An error occurred")
    }
}

