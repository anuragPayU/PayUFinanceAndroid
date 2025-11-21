package com.payu.finance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.payu.finance.ui.screen.EmiScheduleScreen
import com.payu.finance.ui.screen.HistoryScreen
import com.payu.finance.ui.screen.HomeScreen
import com.payu.finance.ui.screen.LoanDetailScreen
import com.payu.finance.ui.screen.LoansScreen
import com.payu.finance.ui.screen.MainScreen
import com.payu.finance.ui.screen.ProfileScreen
import com.payu.finance.ui.screen.RepaymentScreen
import com.payu.finance.ui.screen.RepaymentsScreen
import com.payu.finance.ui.screen.WebViewScreen
import com.payu.finance.ui.viewmodel.HistoryViewModel
import com.payu.finance.ui.viewmodel.HomeViewModel
import com.payu.finance.ui.viewmodel.LoanDetailViewModel
import com.payu.finance.ui.viewmodel.LoansViewModel
import com.payu.finance.ui.viewmodel.RepaymentViewModel
import com.payu.finance.ui.viewmodel.RepaymentsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main Navigation Routes
 */
object MainRoutes {
    const val HOME = "home"
    const val LOANS = "loans"
    const val LOAN_DETAIL = "loan_detail/{loanId}"
    const val REPAYMENTS = "repayments"
    const val REPAYMENT = "repayment/{loanId}"
    const val PROFILE = "profile"
    const val HISTORY = "history"
    const val WEB_VIEW = "web_view/{url}"
    const val EMI_SCHEDULE = "emi_schedule/{loanId}"
    
    fun loanDetail(loanId: String) = "loan_detail/$loanId"
    fun repayment(loanId: String) = "repayment/$loanId"
    fun emiSchedule(loanId: String) = "emi_schedule/$loanId"
    fun webView(url: String): String {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        return "web_view/$encodedUrl"
    }
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
                    onNavigateToRepayment = { loanId ->
                        navController.navigate(MainRoutes.repayment(loanId)) {
                            popUpTo(MainRoutes.HOME) { inclusive = false }
                        }
                    },
                    onNavigateToLoanDetail = { loanId ->
                        navController.navigate(MainRoutes.loanDetail(loanId))
                    },
                    onNavigateToEmiSchedule = { loanId ->
                        navController.navigate(MainRoutes.emiSchedule(loanId))
                    },
                    onNavigateToRepayments = {
                        navController.navigate(MainRoutes.REPAYMENTS) {
                            popUpTo(MainRoutes.HOME) { inclusive = false }
                        }
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
                    onNavigateToWebView = { url ->
                        navController.navigate(MainRoutes.webView(url)) {
                            popUpTo(MainRoutes.PROFILE) { inclusive = false }
                        }
                    },
                    onLogout = {
                        // TODO: Handle logout - navigate to auth screen
                        navController.popBackStack()
                    }
                )
            }
            
            composable(
                route = MainRoutes.WEB_VIEW,
                arguments = listOf(navArgument("url") { type = NavType.StringType })
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
                val url = java.net.URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                WebViewScreen(
                    url = url,
                    onBackClick = { navController.popBackStack() }
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
            
            composable(
                route = MainRoutes.REPAYMENT,
                arguments = listOf(navArgument("loanId") { type = NavType.StringType })
            ) { backStackEntry ->
                val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
                val repaymentViewModel: RepaymentViewModel = koinViewModel()
                
                RepaymentScreen(
                    viewModel = repaymentViewModel,
                    loanId = loanId,
                    onBackClick = { navController.popBackStack() }
                )
            }
            
            composable(MainRoutes.REPAYMENTS) {
                val repaymentsViewModel: RepaymentsViewModel = koinViewModel()
                
                RepaymentsScreen(
                    viewModel = repaymentsViewModel,
                    loanId = null, // Can be made optional if needed
                    modifier = Modifier
                )
            }
            
            composable(
                route = MainRoutes.EMI_SCHEDULE,
                arguments = listOf(navArgument("loanId") { type = NavType.StringType })
            ) { backStackEntry ->
                val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
                
                EmiScheduleScreen(
                    loanId = loanId,
                    onBackClick = { navController.popBackStack() },
                    onPayEmiClick = { emiId ->
                        // Navigate to repayment screen when Pay Now is clicked
                        navController.navigate(MainRoutes.repayment(loanId)) {
                            popUpTo(MainRoutes.EMI_SCHEDULE) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}

