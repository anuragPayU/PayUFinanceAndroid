package com.payu.finance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.payu.finance.ui.screen.HistoryScreen
import com.payu.finance.ui.screen.HomeScreen
import com.payu.finance.ui.screen.LoanDetailScreen
import com.payu.finance.ui.screen.LoansScreen
import com.payu.finance.ui.screen.MainScreen
import com.payu.finance.ui.screen.ProfileScreen
import com.payu.finance.ui.viewmodel.HistoryViewModel
import com.payu.finance.ui.viewmodel.HomeViewModel
import com.payu.finance.ui.viewmodel.LoanDetailViewModel
import com.payu.finance.ui.viewmodel.LoansViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Navigation Routes
 */
object MainRoutes {
    const val HOME = "home"
    const val LOANS = "loans"
    const val LOAN_DETAIL = "loan_detail/{loanId}"
    const val REPAYMENTS = "repayments"
    const val PROFILE = "profile"
    const val HISTORY = "history"
    
    fun loanDetail(loanId: String) = "loan_detail/$loanId"
}

/**
 * Main Navigation Graph
 */
@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainRoutes.HOME
) {
    MainScreen(
        navController = navController,
        modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(MainRoutes.HOME) {
                val homeViewModel: HomeViewModel = koinViewModel()
                
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToLoans = {
                        navController.navigate(MainRoutes.LOANS) {
                            popUpTo(MainRoutes.HOME) { inclusive = false }
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(MainRoutes.PROFILE) {
                            popUpTo(MainRoutes.HOME) { inclusive = false }
                        }
                    },
                    onNavigateToRepayment = { repaymentId ->
                        // TODO: Navigate to repayment screen
                        // navController.navigate("${MainRoutes.REPAYMENTS}/$repaymentId")
                    },
                    onNavigateToLoanDetail = { loanId ->
                        navController.navigate(MainRoutes.loanDetail(loanId))
                    }
                )
            }
            
            composable(MainRoutes.HISTORY) {
                val historyViewModel: HistoryViewModel = koinViewModel()
                
                HistoryScreen(
                    viewModel = historyViewModel,
                    onNavigateToLoanDetail = { loanId ->
                        navController.navigate(MainRoutes.loanDetail(loanId))
                    }
                )
            }
            
            composable(MainRoutes.PROFILE) {
                ProfileScreen(
                    onNavigateToHistory = {
                        navController.navigate(MainRoutes.HISTORY) {
                            popUpTo(MainRoutes.PROFILE) { inclusive = false }
                        }
                    },
                    onNavigateToSettings = {
                        // TODO: Navigate to settings screen
                    },
                    onNavigateToHelp = {
                        // TODO: Navigate to help screen
                    },
                    onNavigateToAbout = {
                        // TODO: Navigate to about screen
                    },
                    onLogout = {
                        // TODO: Handle logout
                    }
                )
            }
            
            composable(MainRoutes.LOANS) {
                val loansViewModel: LoansViewModel = koinViewModel()
                
                LoansScreen(
                    viewModel = loansViewModel
                )
            }
            
            composable(
                route = MainRoutes.LOAN_DETAIL,
                arguments = listOf(navArgument("loanId") { type = NavType.StringType })
            ) { backStackEntry ->
                val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
                val loanDetailViewModel: LoanDetailViewModel = koinViewModel()
                
                LoanDetailScreen(
                    viewModel = loanDetailViewModel,
                    loanId = loanId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

