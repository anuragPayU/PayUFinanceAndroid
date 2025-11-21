package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.common.Resource
import com.payu.finance.ui.model.ProfileMenuItem
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.viewmodel.ProfileEvent
import com.payu.finance.ui.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Profile Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onNavigateToWebView: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val profileResource by viewModel.profileResource.collectAsState()
    val showLogoutBottomSheet by viewModel.showLogoutBottomSheet.collectAsState()

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(paddingValues)
        ) {
            when (val resource = profileResource) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Error -> {
                    ErrorView(
                        message = resource.message ?: "An error occurred",
                        onRetry = { viewModel.handleEvent(ProfileEvent.Refresh) }
                    )
                }
                is Resource.Success -> {
                    val profileState = resource.data ?: return@Box
                    ProfileContent(
                        profileState = profileState,
                        onMenuItemClick = { menuItem ->
                            when (menuItem.actionType) {
                                "WEBLINK" -> {
                                    menuItem.actionUrl?.let { url ->
                                        onNavigateToWebView(url)
                                    }
                                }
                                "bottomSheet" -> {
                                    viewModel.handleEvent(ProfileEvent.ShowLogoutBottomSheet)
                                }
                            }
                        },
                        onLogoutClick = {
                            viewModel.handleEvent(ProfileEvent.ShowLogoutBottomSheet)
                        }
                    )
                }
            }
        }
    }

    // Logout Bottom Sheet
    if (showLogoutBottomSheet) {
        val logoutBottomSheet = (profileResource as? Resource.Success)?.data?.logoutBottomSheet
        if (logoutBottomSheet != null) {
            LogoutBottomSheetDialog(
                title = logoutBottomSheet.title,
                subtitle = logoutBottomSheet.subtitle,
                primaryActionText = logoutBottomSheet.primaryActionText,
                secondaryActionText = logoutBottomSheet.secondaryActionText,
                onDismiss = {
                    viewModel.handleEvent(ProfileEvent.DismissLogoutBottomSheet)
                },
                onConfirmLogout = {
                    viewModel.handleEvent(ProfileEvent.ConfirmLogout)
                    onLogout()
                }
            )
        }
    }
}

/**
 * Profile Content
 */
@Composable
fun ProfileContent(
    profileState: com.payu.finance.ui.model.ProfileUiState,
    onMenuItemClick: (ProfileMenuItem) -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PayUFinanceColors.BackgroundPrimary),
        contentPadding = PaddingValues(bottom = Spacing40)
    ) {
        // Profile Header Section
        item {
            ProfileHeader(
                userName = profileState.userName,
                userPhone = profileState.userPhone,
                userDescription = profileState.userDescription,
                appVersionLabel = profileState.appVersionLabel,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PayUFinanceColors.PrimaryLight)
                    .padding(
                        horizontal = Spacing40,
                        vertical = Spacing70
                    )
            )
        }

        // Profile Options Section
        item {
            Spacer(modifier = Modifier.height(Spacing30))
        }

        item {
            ProfileOptionsCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing40),
                menuItems = profileState.menuItems,
                onMenuItemClick = onMenuItemClick
            )
        }

        // Spacer to push logout button to bottom
        item {
            Spacer(modifier = Modifier.height(Spacing30))
        }

        // Logout Button (if not in menu items)
        val hasLogoutInMenu = profileState.menuItems.any { 
            it.title.equals("Sign out", ignoreCase = true) || 
            it.actionType == "bottomSheet"
        }
        if (!hasLogoutInMenu) {
            item {
                LogoutButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing40),
                    onLogout = onLogoutClick
                )
            }
        }
    }
}

/**
 * Profile Header Component
 */
@Composable
fun ProfileHeader(
    userName: String,
    userPhone: String,
    userDescription: String? = null,
    appVersionLabel: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PayUFinanceColors.Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = PayUFinanceColors.BackgroundPrimary,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(Spacing30))

        // User Name
        ElevateText(
            markup = userName,
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary
        )

        Spacer(modifier = Modifier.height(Spacing10))

        // User Phone
        ElevateText(
            markup = userPhone,
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentSecondary
        )

        // User Description (e.g., "KYC Verified")
        if (!userDescription.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(Spacing10))
            ElevateText(
                markup = userDescription,
                style = LpTypography.BodySmall,
                color = PayUFinanceColors.ContentSecondary
            )
        }

        // App Version Label
        if (!appVersionLabel.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(Spacing30))
            ElevateText(
                markup = appVersionLabel,
                style = LpTypography.BodySmall,
                color = PayUFinanceColors.ContentTertiary
            )
        }
    }
}

/**
 * Profile Options Card - Groups all menu items in a single card
 */
@Composable
fun ProfileOptionsCard(
    modifier: Modifier = Modifier,
    menuItems: List<ProfileMenuItem>,
    onMenuItemClick: (ProfileMenuItem) -> Unit = {}
) {
    Card(
        modifier = modifier
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            ),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.BackgroundPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            menuItems.forEachIndexed { index, menuItem ->
                ProfileOptionItem(
                    title = menuItem.title,
                    onClick = { onMenuItemClick(menuItem) },
                    showDivider = index < menuItems.size - 1
                )
            }
        }
    }
}

/**
 * Profile Option Item Component
 */
@Composable
fun ProfileOptionItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevateText(
                markup = title,
                style = LpTypography.BodyNormal,
                color = PayUFinanceColors.ContentPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = PayUFinanceColors.ContentTertiary,
                modifier = Modifier.size(20.dp)
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing40),
                color = PayUFinanceColors.BorderPrimary,
                thickness = 1.dp
            )
        }
    }
}

/**
 * Logout Button Component
 */
@Composable
fun LogoutButton(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.Error.copy(alpha = 0.2f),
                shape = RoundedCornerShape(Spacing30)
            )
            .clickable(onClick = onLogout),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.ErrorBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout",
                tint = PayUFinanceColors.Error,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = Spacing20)
            )
            ElevateText(
                markup = "Logout",
                style = LpTypography.TitleSecondary,
                color = PayUFinanceColors.Error
            )
        }
    }
}

/**
 * Logout Bottom Sheet Dialog
 */
@Composable
fun LogoutBottomSheetDialog(
    title: String,
    subtitle: String,
    primaryActionText: String,
    secondaryActionText: String,
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            ElevateText(
                markup = title,
                style = LpTypography.TitleSection,
                color = PayUFinanceColors.ContentPrimary
            )
        },
        text = {
            ElevateText(
                markup = subtitle,
                style = LpTypography.BodyNormal,
                color = PayUFinanceColors.ContentSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmLogout
            ) {
                ElevateText(
                    markup = primaryActionText,
                    style = LpTypography.TitleSecondary,
                    color = PayUFinanceColors.Error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                ElevateText(
                    markup = secondaryActionText,
                    style = LpTypography.BodyNormal,
                    color = PayUFinanceColors.ContentPrimary
                )
            }
        }
    )
}


// Preview
@Preview(showBackground = true, name = "Profile Screen Preview")
@Composable
private fun ProfileScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        // Preview with mock data
        ProfileContent(
            profileState = com.payu.finance.ui.model.ProfileUiState(
                userName = "Hi, Rahul",
                userPhone = "+91 9988776655·",
                userDescription = "KYC Verified",
                appVersionLabel = "App Version: 7.6.6",
                menuItems = listOf(
                    com.payu.finance.ui.model.ProfileMenuItem(
                        title = "Privacy policy",
                        actionType = "WEBLINK",
                        actionUrl = "web url",
                        leadingIcon = null,
                        trailingIcon = null
                    ),
                    com.payu.finance.ui.model.ProfileMenuItem(
                        title = "DPO Terms & Conditions",
                        actionType = "WEBLINK",
                        actionUrl = "web url",
                        leadingIcon = null,
                        trailingIcon = null
                    ),
                    com.payu.finance.ui.model.ProfileMenuItem(
                        title = "Terms & Conditions",
                        actionType = "WEBLINK",
                        actionUrl = "web url",
                        leadingIcon = null,
                        trailingIcon = null
                    ),
                    com.payu.finance.ui.model.ProfileMenuItem(
                        title = "Grievance redressal policy",
                        actionType = "WEBLINK",
                        actionUrl = "web url",
                        leadingIcon = null,
                        trailingIcon = null
                    ),
                    com.payu.finance.ui.model.ProfileMenuItem(
                        title = "Sign out",
                        actionType = "bottomSheet",
                        actionUrl = "url",
                        leadingIcon = null,
                        trailingIcon = null
                    )
                )
            )
        )
    }
}
