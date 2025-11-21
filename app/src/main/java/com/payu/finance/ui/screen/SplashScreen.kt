package com.payu.finance.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.payu.finance.R
import com.payu.finance.ui.viewmodel.SplashViewModel

/**
 * Splash Screen - Checks authentication status and navigates accordingly
 */
@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val authResource by viewModel.authResource.collectAsState()
    
    LaunchedEffect(authResource) {
        when (authResource) {
            is com.payu.finance.common.Resource.Success -> {
                if (authResource.data == true) {
                    // User is authenticated, navigate to home
                    onNavigateToHome()
                } else {
                    // User is not authenticated, navigate to auth
                    onNavigateToAuth()
                }
            }
            is com.payu.finance.common.Resource.Error -> {
                // Authentication failed, navigate to auth
                onNavigateToAuth()
            }
            is com.payu.finance.common.Resource.Loading -> {
                // Still loading, show loading indicator
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Spacer to push content to center
            Spacer(modifier = Modifier.weight(1f))
            
            // App Icon
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier.size(120.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Loading Indicator
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            
            // Spacer to push content to center
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

