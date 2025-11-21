package com.payu.finance.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Custom PayU Finance Color Palette
 * 
 * IMPORTANT: Update all hex color values below to match your Figma design
 * Figma Links:
 * - Main colors: https://www.figma.com/design/rMFmIgkArDKJ0KIoFuqv7X/PayU-Fin-App?node-id=4808-2594&m=dev
 * - Hyperlink: https://www.figma.com/design/rMFmIgkArDKJ0KIoFuqv7X/PayU-Fin-App?node-id=4805-10993&m=dev
 * - Button: https://www.figma.com/design/rMFmIgkArDKJ0KIoFuqv7X/PayU-Fin-App?node-id=4805-10942&m=dev
 * 
 * To get colors from Figma:
 * 1. Select an element in Figma
 * 2. Check the Fill color in the right panel
 * 3. Copy the hex value (e.g., #0066FF)
 * 4. Convert to Android format: 0xFF0066FF (replace # with 0xFF)
 */
object PayUFinanceColors {
    // Primary Colors - FROM FIGMA
    // Figma link: https://www.figma.com/design/rMFmIgkArDKJ0KIoFuqv7X/PayU-Fin-App?node-id=4805-10942&m=dev
    val Primary = Color(0xFF0ABF89) // Primary brand color from Figma
    val PrimaryDark = Color(0xFF089A6F) // Darker shade for pressed states
    val PrimaryLight = Color(0xFFE6F9F5) // Light background for primary
    
    // Background Colors - UPDATE FROM FIGMA
    val BackgroundPrimary = Color(0xFFFFFFFF) // Main background color from Figma - UPDATE THIS
    val BackgroundSecondary = Color(0xFFF8F9FA) // Card background color from Figma - UPDATE THIS
    val BackgroundTertiary = Color(0xFFE9ECEF) // Border/Light gray from Figma - UPDATE THIS
    
    // Content/Text Colors - UPDATE FROM FIGMA
    val ContentPrimary = Color(0xFF1A1A1A) // Primary text color from Figma - UPDATE THIS
    val ContentSecondary = Color(0xFF6C757D) // Secondary text color from Figma - UPDATE THIS
    val ContentTertiary = Color(0xFFADB5BD) // Tertiary text color from Figma - UPDATE THIS
    val ContentInactive = Color(0xFFCED4DA) // Inactive text color from Figma - UPDATE THIS
    val Hyperlink = Color(0xFF0ABF89) // Hyperlink color from Figma (node-id=4805-10993)
    
    // Status Colors
    val Success = Color(0xFF10B981) // Green for success/paid
    val SuccessBackground = Color(0xFFECFDF5) // Light green background
    val Warning = Color(0xFFF59E0B) // Orange for warning/pending
    val WarningBackground = Color(0xFFFFFBEB) // Light orange background
    val Error = Color(0xFFDA1E28) // Red for error/overdue
    val ErrorBackground = Color(0xFFFFF1F1) // Light red background
    
    // Border Colors - UPDATE FROM FIGMA
    val BorderPrimary = Color(0xFFDEE2E6) // Default border color from Figma - UPDATE THIS
    val BorderSelected = Color(0xFF1A1A1A) // Selected border color from Figma - UPDATE THIS
    val BorderBrand = Color(0xFF0ABF89) // Brand border color from Figma (should match Primary)
    
    // Card Colors - UPDATE FROM FIGMA
    val CardBackground = Color(0xFFF8F9FA) // Card background color from Figma - UPDATE THIS
    val CardBackgroundOverdue = Color(0xFFFFF5F5) // Overdue card background from Figma - UPDATE THIS
    
    // Progress Bar - UPDATE FROM FIGMA
    val ProgressBarTrack = Color(0xFFE9ECEF) // Progress bar track color from Figma - UPDATE THIS
    val ProgressBarFill = Color(0xFF0ABF89) // Progress bar fill color from Figma (should match Primary)
}

