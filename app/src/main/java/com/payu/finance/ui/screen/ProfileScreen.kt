package com.payu.finance.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.ui.theme.PayUFinanceColors

/**
 * Profile Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToHelp: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .padding(paddingValues)
        ) {
            // Profile Header
            ProfileHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PayUFinanceColors.PrimaryLight)
                    .padding(Spacing40)
            )

            Spacer(modifier = Modifier.height(Spacing30))

            // Profile Options
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing40)
            ) {
                ProfileOptionItem(
                    title = "History",
                    icon = Icons.Default.List,
                    onClick = onNavigateToHistory
                )
                
                Divider(modifier = Modifier.padding(vertical = Spacing10))
                
                ProfileOptionItem(
                    title = "Settings",
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToSettings
                )
                
                Divider(modifier = Modifier.padding(vertical = Spacing10))
                
                ProfileOptionItem(
                    title = "Help & Support",
                    icon = Icons.Default.Info,
                    onClick = onNavigateToHelp
                )
                
                Divider(modifier = Modifier.padding(vertical = Spacing10))
                
                ProfileOptionItem(
                    title = "About",
                    icon = Icons.Default.Info,
                    onClick = onNavigateToAbout
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing40)
                    .clickable(onClick = onLogout),
                shape = RoundedCornerShape(Spacing30),
                colors = CardDefaults.cardColors(
                    containerColor = PayUFinanceColors.ErrorBackground
                )
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
                        modifier = Modifier.padding(end = Spacing20)
                    )
                    ElevateText(
                        markup = "Logout",
                        style = LpTypography.TitleSecondary,
                        color = PayUFinanceColors.Error
                    )
                }
            }
        }
    }
}

/**
 * Profile Header Component
 */
@Composable
fun ProfileHeader(
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
            markup = "John Doe", // TODO: Replace with actual user name
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary
        )

        Spacer(modifier = Modifier.height(Spacing10))

        // User Phone/Email
        ElevateText(
            markup = "+91 98765 43210", // TODO: Replace with actual user phone/email
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentSecondary
        )
    }
}

/**
 * Profile Option Item Component
 */
@Composable
fun ProfileOptionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Spacing20),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing30),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = PayUFinanceColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                ElevateText(
                    markup = title,
                    style = LpTypography.BodyNormal,
                    color = PayUFinanceColors.ContentPrimary
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = PayUFinanceColors.ContentTertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// Preview
@Preview(showBackground = true, name = "Profile Screen Preview")
@Composable
private fun ProfileScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        ProfileScreen()
    }
}

