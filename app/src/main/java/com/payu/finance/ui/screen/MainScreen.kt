package com.payu.finance.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.payu.finance.R
import com.payu.finance.navigation.MainRoutes
import com.payu.finance.ui.theme.PayUFinanceColors

/**
 * Bottom Navigation Tabs
 */
sealed class BottomNavTab(
    val route: String,
    val title: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
) {
    object Home : BottomNavTab(
        MainRoutes.HOME, 
        "Home", 
        R.drawable.ic_home_selected,
        R.drawable.ic_home_unselected
    )
    object History : BottomNavTab(
        MainRoutes.HISTORY, 
        "History", 
        R.drawable.ic_history_selected,
        R.drawable.ic_history_unselected
    )
    object Profile : BottomNavTab(
        MainRoutes.PROFILE, 
        "Profile", 
        R.drawable.ic_profile_selected,
        R.drawable.ic_profile_unselected
    )
}

/**
 * Main Screen Wrapper with Bottom Navigation
 * This wrapper provides a common navigation bar for Home, History, and Profile screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val currentRoute = navController.currentDestination?.route ?: MainRoutes.HOME
    var selectedTab by remember { mutableStateOf(getTabForRoute(currentRoute)) }

    // Update selected tab when route changes
    LaunchedEffect(currentRoute) {
        selectedTab = getTabForRoute(currentRoute)
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp), // Disable automatic window insets for edge-to-edge
        bottomBar = {
            Column {
                // Top border/divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(PayUFinanceColors.BorderPrimary)
                )
                NavigationBar(
                    containerColor = PayUFinanceColors.BackgroundPrimary,
                    tonalElevation = 8.dp
                ) {
                    getAllBottomNavTabs().forEach { tab ->
                        val isSelected = selectedTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                selectedTab = tab
                                navController.navigate(tab.route) {
                                    // Pop up to the start destination to avoid building up a back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected tab
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = if (isSelected) tab.selectedIcon else tab.unselectedIcon
                                    ),
                                    contentDescription = tab.title,
                                    tint = Color.Unspecified // Use drawable's built-in colors
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) {
                                            PayUFinanceColors.Primary
                                        } else {
                                            PayUFinanceColors.ContentInactive
                                        }
                                    )
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PayUFinanceColors.Primary,
                                selectedTextColor = PayUFinanceColors.Primary,
                                indicatorColor = Color.Transparent, // Remove default indicator
                                unselectedIconColor = PayUFinanceColors.ContentInactive,
                                unselectedTextColor = PayUFinanceColors.ContentInactive
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding() // Add status bar padding
                .padding(paddingValues) // Use Scaffold's padding for bottom navigation bar
        ) {
            content()
        }
    }
}

/**
 * Helper function to get tab for route
 */
private fun getTabForRoute(route: String): BottomNavTab {
    return when {
        route == MainRoutes.HOME -> BottomNavTab.Home
        route == MainRoutes.HISTORY -> BottomNavTab.History
        route == MainRoutes.PROFILE -> BottomNavTab.Profile
        else -> BottomNavTab.Home
    }
}

/**
 * Helper function to get all BottomNavTab values
 */
private fun getAllBottomNavTabs(): List<BottomNavTab> {
    return listOf(
        BottomNavTab.Home,
        BottomNavTab.History,
        BottomNavTab.Profile
    )
}

