package com.payu.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.payu.finance.navigation.AuthNavigation
import com.payu.finance.ui.theme.PayUFinanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PayUFinanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Start with Auth flow
                    AuthNavigation(
                        onAuthSuccess = { token ->
                            // TODO: Navigate to main app after successful authentication
                            // For now, token is received and can be stored
                        }
                    )
                }
            }
        }
    }
}

