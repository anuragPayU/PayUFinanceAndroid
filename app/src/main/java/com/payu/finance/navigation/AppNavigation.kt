package com.payu.finance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.payu.finance.ui.screen.SplashScreen
import com.payu.finance.ui.viewmodel.SplashViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * App-level navigation routes
 */
object AppRoutes {
    const val SPLASH = "splash"
    const val AUTH = "auth"
    const val MAIN = "main"
}

/**
 * Root Navigation Graph
 * Handles navigation between Splash, Auth, and Main flows
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf(AppRoutes.SPLASH) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(AppRoutes.SPLASH) {
            val splashViewModel: SplashViewModel = koinViewModel()
            
            SplashScreen(
                viewModel = splashViewModel,
                onNavigateToAuth = {
                    navController.navigate(AppRoutes.AUTH) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppRoutes.MAIN) {
                        popUpTo(AppRoutes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppRoutes.AUTH) {
            AuthNavigation(
                onAuthSuccess = { token ->
                    // Navigate to main app after successful authentication
                    navController.navigate(AppRoutes.MAIN) {
                        popUpTo(AppRoutes.AUTH) { inclusive = true }
                    }
                }
            )
        }
        
        composable(AppRoutes.MAIN) {
            MainNavigation()
        }
    }
}

