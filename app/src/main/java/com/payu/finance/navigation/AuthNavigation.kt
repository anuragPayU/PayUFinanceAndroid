package com.payu.finance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.payu.finance.ui.screen.WebViewScreen
import com.payu.finance.ui.screen.auth.MobileInputScreen
import com.payu.finance.ui.screen.auth.OtpScreen
import com.payu.finance.ui.viewmodel.MobileInputViewModel
import com.payu.finance.ui.viewmodel.OtpViewModel
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder
import java.net.URLDecoder

/**
 * Navigation routes for Auth flow
 */
object AuthRoutes {
    const val MOBILE_INPUT = "mobile_input"
    const val OTP_VERIFICATION = "otp_verification"
    const val WEB_VIEW = "web_view/{url}/{title}"
    
    const val MOBILE_NUMBER_ARG = "mobileNumber"
    const val URL_ARG = "url"
    const val TITLE_ARG = "title"
    
    fun otpRoute(mobileNumber: String) = "$OTP_VERIFICATION/${encodeForRoute(mobileNumber)}"
    fun webViewRoute(url: String, title: String) = "web_view/${encodeForRoute(url)}/${encodeForRoute(title)}"
    
    private fun encodeForRoute(text: String): String {
        return URLEncoder.encode(text, "UTF-8")
    }
}

/**
 * Auth Navigation Graph
 */
@Composable
fun AuthNavigation(
    navController: NavHostController = rememberNavController(),
    onAuthSuccess: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.MOBILE_INPUT
    ) {
        composable(AuthRoutes.MOBILE_INPUT) {
            val viewModel: MobileInputViewModel = koinViewModel()
            
            MobileInputScreen(
                viewModel = viewModel,
                onOtpSent = { mobileNumber ->
                    navController.navigate(AuthRoutes.otpRoute(mobileNumber))
                },
                onOpenPrivacyPolicy = {
                    navController.navigate(AuthRoutes.webViewRoute(it, "Privacy Policy"))
                },
                onOpenTermsAndConditions = {
                    navController.navigate(AuthRoutes.webViewRoute(it, "Terms & Conditions"))
                }
            )
        }
        
        composable(
            route = "${AuthRoutes.OTP_VERIFICATION}/{${AuthRoutes.MOBILE_NUMBER_ARG}}",
            arguments = listOf(
                navArgument(AuthRoutes.MOBILE_NUMBER_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val mobileNumberArg = backStackEntry.arguments?.getString(AuthRoutes.MOBILE_NUMBER_ARG)
            
            // Ensure mobileNumber is always provided - navigate back if missing
            if (mobileNumberArg.isNullOrEmpty()) {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
                return@composable
            }
            
            // Decode mobile number from route (in case it was URL encoded)
            val mobileNumber = mobileNumberArg.decodeFromRoute()
            val viewModel: OtpViewModel = koinViewModel()
            
            // Set mobile number once when screen is created, using DisposableEffect to ensure it only runs once
            DisposableEffect(mobileNumber) {
                if (viewModel.uiState.value.mobileNumber.isEmpty()) {
                    viewModel.handleEvent(com.payu.finance.ui.viewmodel.OtpEvent.SetMobileNumber(mobileNumber))
                }
                onDispose { }
            }
            
            OtpScreen(
                viewModel = viewModel,
                onOtpVerified = { token ->
                    onAuthSuccess(token)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = AuthRoutes.WEB_VIEW,
            arguments = listOf(
                navArgument(AuthRoutes.URL_ARG) {
                    type = NavType.StringType
                },
                navArgument(AuthRoutes.TITLE_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString(AuthRoutes.URL_ARG)?.decodeFromRoute() ?: ""
            val title = backStackEntry.arguments?.getString(AuthRoutes.TITLE_ARG)?.decodeFromRoute() ?: "Web View"
            
            WebViewScreen(
                url = url,
                title = title,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

private fun String.decodeFromRoute(): String {
    return try {
        URLDecoder.decode(this, "UTF-8")
    } catch (e: Exception) {
        this
    }
}

