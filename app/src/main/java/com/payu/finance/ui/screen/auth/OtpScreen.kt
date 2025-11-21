package com.payu.finance.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ButtonDefaults
import com.lazypay.android.elevate.Elevate
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.common.Resource
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.viewmodel.OtpEvent
import com.payu.finance.ui.viewmodel.OtpViewModel

/**
 * OTP Input Screen (6 digits)
 * Following Elevate design system
 */
@Composable
fun OtpScreen(
    viewModel: OtpViewModel,
    onOtpVerified: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val verifyOtpResource by viewModel.verifyOtpResource.collectAsState()

    // Navigate when OTP is verified successfully
    LaunchedEffect(verifyOtpResource) {
        if (verifyOtpResource is Resource.Success) {
            verifyOtpResource.data?.let { token ->
                onOtpVerified(token)
            }
        }
    }

    // Auto-submit when OTP is complete
    LaunchedEffect(uiState.isOtpComplete) {
        if (uiState.isOtpComplete && uiState.error == null) {
            viewModel.handleEvent(OtpEvent.VerifyOtp)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundPrimary)
            .statusBarsPadding()
            .padding(horizontal = Spacing40),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing70))

            // Title and Subtitle
            uiState.content?.let { content ->
                ElevateText(
                    markup = content.title,
                    style = LpTypography.TitleHeader,
                    color = ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )

                // Subtitle with mobile number
                ElevateText(
                    markup = "${content.subtitlePrefix}+91 ${uiState.mobileNumber}${content.subtitleSuffix}",
                    style = LpTypography.BodyNormal,
                    color = ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing40)
                )
            } ?: run {
                // Fallback
                ElevateText(
                    markup = "Enter OTP",
                    style = LpTypography.TitleHeader,
                    color = ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = "We've sent an OTP to +91 ${uiState.mobileNumber}",
                    style = LpTypography.BodyNormal,
                    color = ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing40)
                )
            }

            // OTP Input Field
            OtpInputField(
                otp = uiState.otp,
                error = uiState.error,
                onOtpChanged = { otp ->
                    viewModel.handleEvent(OtpEvent.OtpChanged(otp))
                },
                modifier = Modifier.padding(vertical = Spacing20)
            )

            // Error message
            uiState.error?.let { errorMessage ->
                Spacer(modifier = Modifier.height(Spacing10))
                ElevateText(
                    markup = errorMessage,
                    style = LpTypography.BodySmall,
                    color = ContentNegative,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Resend OTP option
            uiState.content?.let { content ->
                Spacer(modifier = Modifier.height(Spacing30))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ElevateText(
                        markup = content.resendOtpPrefix,
                        style = LpTypography.BodyNormal,
                        color = ContentSecondary
                    )
                    ElevateText(
                        markup = content.resendOtpLinkText,
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.Hyperlink,
                        modifier = Modifier.clickable {
                            // TODO: Implement resend OTP
                        }
                    )
                }
            }
        }

        // Verify Button - moves above keyboard when shown
        LPButton(
            text = uiState.content?.verifyButtonText ?: "Verify",
            state = if (uiState.isOtpComplete && !uiState.isLoading) {
                ButtonState.Enabled
            } else if (uiState.isLoading) {
                ButtonState.Loading
            } else {
                ButtonState.Disabled
            },
            backgroundColor = PayUFinanceColors.Primary,
            pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
            disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
            contentColor = PayUFinanceColors.BackgroundPrimary,
            pressedContentColor = PayUFinanceColors.BackgroundPrimary,
            disabledContentColor = PayUFinanceColors.ContentTertiary,
            circularProgressColor = PayUFinanceColors.BackgroundPrimary,
            buttonElevation = ButtonDefaults.elevation(),
            onClick = {
                viewModel.handleEvent(OtpEvent.VerifyOtp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(bottom = Spacing40)
        )
    }
}

@Composable
fun OtpInputField(
    otp: String,
    error: String?,
    onOtpChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    otpLength: Int = 6
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val isError = error != null

    // Hidden TextField for input
    TextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .alpha(0f)
            .height(0.dp),
        value = otp,
        onValueChange = { newValue ->
            if (newValue.length <= otpLength && newValue.all { it.isDigit() }) {
                onOtpChanged(newValue)
                if (newValue.length == otpLength) {
                    keyboardController?.hide()
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    // OTP Cells
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (0 until otpLength).forEach { index ->
            val currentChar = otp.getOrNull(index)
            val isCurrentCell = otp.length == index
            val showBoldBorder = currentChar != null || isCurrentCell

            OtpCell(
                value = currentChar?.toString() ?: "",
                isError = isError,
                showBoldBorder = showBoldBorder,
                modifier = Modifier
                    .width(48.dp)
                    .height(60.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
            )

            if (index < otpLength - 1) {
                Spacer(modifier = Modifier.width(Spacing20))
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun OtpCell(
    value: String,
    isError: Boolean,
    showBoldBorder: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(
                width = if (showBoldBorder || isError) 2.dp else 1.dp,
                color = when {
                    isError -> BorderNegative
                    showBoldBorder -> BorderSelected
                    else -> BorderPrimary
                },
                shape = RoundedCornerShape(Spacing30)
            )
            .clip(RoundedCornerShape(Spacing30))
            .background(BackgroundPrimary)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = LpTypography.InputPlaceholder,
            color = ContentPrimary
        )
    }
}

// Preview Composable
@Preview(showBackground = true, name = "OTP Screen Preview")
@Composable
private fun OtpScreenPreview() {
    OtpInputField(
        otp = "123456",
        error = null,
        onOtpChanged = {},
        modifier = Modifier.padding(vertical = Spacing20)
    )
}

