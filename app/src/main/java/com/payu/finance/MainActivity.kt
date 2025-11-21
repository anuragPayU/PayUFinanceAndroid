package com.payu.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.payu.finance.navigation.AppNavigation
import com.payu.finance.ui.theme.PayUFinanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge before setting content
        enableEdgeToEdge()
        
        // Ensure window decor fits system windows is false for true edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        setContent {
            PayUFinanceTheme {
                // No Surface wrapper - let content handle backgrounds for true edge-to-edge
                // Start with Splash screen to check authentication
                AppNavigation()
            }
        }
    }
}

