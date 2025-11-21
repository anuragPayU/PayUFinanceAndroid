package com.payu.finance.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.payu.finance.navigation.MainRoutes

/**
 * Bottom Navigation Tabs
 */
sealed class BottomNavTab(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavTab(MainRoutes.HOME, "Home", Icons.Default.Home)
    object History : BottomNavTab(MainRoutes.HISTORY, "History", Icons.Default.List)
    object Profile : BottomNavTab(MainRoutes.PROFILE, "Profile", Icons.Default.Person)
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
        bottomBar = {
            NavigationBar {
                getAllBottomNavTabs().forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
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
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

