package com.payu.finance.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import com.payu.finance.R
import com.lazypay.android.elevate.Elevate
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import com.payu.finance.common.Resource
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.model.ConsentTextData
import com.payu.finance.ui.model.EnterMobileScreenData
import com.payu.finance.ui.model.ErrorMessagesData
import com.payu.finance.ui.viewmodel.MobileInputEvent
import com.payu.finance.ui.viewmodel.MobileInputViewModel
import com.payu.finance.ui.viewmodel.MobileInputUiState

/**
 * Mobile Input Screen with error state support
 * Following Elevate design system
 */
@Composable
fun MobileInputScreen(
    viewModel: MobileInputViewModel,
    onOtpSent: (String) -> Unit,
    onOpenPrivacyPolicy: (String) -> Unit = {},
    onOpenTermsAndConditions: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState: MobileInputUiState by viewModel.uiState.collectAsState()
    val sendOtpResource by viewModel.sendOtpResource.collectAsState()
    val screenContent: EnterMobileScreenData? = uiState.content

    // Reset sendOtpResource when screen is entered (to prevent re-navigation when coming back from OTP screen)
    LaunchedEffect(Unit) {
        viewModel.handleEvent(MobileInputEvent.ResetSendOtpResource)
    }

    // Navigate to OTP screen when OTP is sent successfully
    LaunchedEffect(sendOtpResource) {
        if (sendOtpResource is Resource.Success) {
            onOtpSent(uiState.mobileNumber)
        }
        // Error handling is done in ViewModel, which updates uiState.error
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
            Spacer(modifier = Modifier.height(56.dp))
            Spacer(modifier = Modifier.height(Spacing70))

            // Show loading or content
            if (screenContent != null) {
                val contentData = screenContent
                // Title
                ElevateText(
                    markup = contentData.title,
                    style = LpTypography.TitleHeader,
                    color = ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )

                Spacer(modifier = Modifier.height(Spacing20))
                // Subtitle
                ElevateText(
                    markup = contentData.subtitle,
                    style = LpTypography.BodyNormal,
                    color = ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing40)
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Mobile Input Field
                MobileInputField(
                    mobileNumber = uiState.mobileNumber,
                    error = uiState.error,
                    label = contentData.mobileNumberLabel,
                    placeholder = contentData.mobileNumberPlaceholder,
                    countryCode = contentData.countryCode,
                    onMobileNumberChanged = { mobileNumber ->
                        viewModel.handleEvent(MobileInputEvent.MobileNumberChanged(mobileNumber))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (!uiState.isLoadingContent) {
                // Fallback content from string resources
                ElevateText(
                    markup = stringResource(R.string.mobile_input_title),
                    style = LpTypography.TitleHeader,
                    color = ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )

                Spacer(modifier = Modifier.height(Spacing20))
                ElevateText(
                    markup = stringResource(R.string.mobile_input_subtitle),
                    style = LpTypography.BodyNormal,
                    color = ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing40)
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Mobile Input Field with string resources
                MobileInputField(
                    mobileNumber = uiState.mobileNumber,
                    error = uiState.error,
                    label = stringResource(R.string.mobile_input_label),
                    placeholder = stringResource(R.string.mobile_input_placeholder),
                    countryCode = "+91",
                    onMobileNumberChanged = { mobileNumber ->
                        viewModel.handleEvent(MobileInputEvent.MobileNumberChanged(mobileNumber))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

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
        }

        // Consent Text with clickable links
        if (screenContent != null) {
            val contentData = screenContent
            ConsentText(
                content = contentData.consentText,
                onPrivacyPolicyClick = { onOpenPrivacyPolicy(contentData.consentText.privacyPolicyUrl) },
                onTermsAndConditionsClick = { onOpenTermsAndConditions(contentData.consentText.termsAndConditionsUrl) },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = Spacing10)
                    .padding(bottom = Spacing20)
            )
        } else if (!uiState.isLoadingContent) {
            // Fallback consent text from string resources
            ConsentText(
                content = ConsentTextData(
                    prefixText = stringResource(R.string.mobile_input_consent_prefix),
                    privacyPolicyLinkText = stringResource(R.string.mobile_input_privacy_policy_link),
                    middleText = stringResource(R.string.mobile_input_consent_middle),
                    termsAndConditionsLinkText = stringResource(R.string.mobile_input_terms_link),
                    privacyPolicyUrl = "", // Will be handled by navigation
                    termsAndConditionsUrl = "" // Will be handled by navigation
                ),
                onPrivacyPolicyClick = { /* Handle navigation */ },
                onTermsAndConditionsClick = { /* Handle navigation */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = Spacing10)
                    .padding(bottom = Spacing20)
            )
        }

        // Continue Button - moves above keyboard when shown
        LPButton(
            text = screenContent?.continueButtonText ?: stringResource(R.string.mobile_input_continue_button),
            state = if (uiState.isMobileValid && !uiState.isLoading) {
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
                // Validate and send OTP - navigation happens in LaunchedEffect when successful
                viewModel.handleEvent(MobileInputEvent.SendOtp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(bottom = Spacing40)
        )
    }
}

/**
 * Consent Text with clickable Privacy Policy and Terms & Conditions links
 */
@Composable
fun ConsentText(
    content: ConsentTextData,
    onPrivacyPolicyClick: () -> Unit,
    onTermsAndConditionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val consentText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = ContentSecondary)) {
            append(content.prefixText)
        }
        
        // Privacy Policy link
        pushStringAnnotation(
            tag = "PRIVACY_POLICY",
            annotation = "privacy_policy"
        )
        withStyle(
            style = SpanStyle(
                color = PayUFinanceColors.Hyperlink,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(content.privacyPolicyLinkText)
        }
        pop()
        
        withStyle(style = SpanStyle(color = ContentSecondary)) {
            append(content.middleText)
        }
        
        // Terms & Conditions link
        pushStringAnnotation(
            tag = "TERMS_AND_CONDITIONS",
            annotation = "terms_and_conditions"
        )
        withStyle(
            style = SpanStyle(
                color = PayUFinanceColors.Hyperlink,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(content.termsAndConditionsLinkText)
        }
        pop()
    }

    ClickableText(
        text = consentText,
        onClick = { offset ->
            consentText.getStringAnnotations(
                tag = "PRIVACY_POLICY",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onPrivacyPolicyClick()
            }
            
            consentText.getStringAnnotations(
                tag = "TERMS_AND_CONDITIONS",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onTermsAndConditionsClick()
            }
        },
        style = LpTypography.BodySmall.copy(color = ContentSecondary, textAlign = TextAlign.Center),
        modifier = modifier
    )
}

@Composable
fun MobileInputField(
    mobileNumber: String,
    error: String?,
    label: String,
    placeholder: String,
    countryCode: String,
    onMobileNumberChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = mobileNumber,
        onValueChange = { newValue ->
            // Limit to max 10 digits
            val digitsOnly = newValue.filter { it.isDigit() }
            if (digitsOnly.length <= 10) {
                onMobileNumberChanged(digitsOnly)
            }
        },
        label = {
            Text(
                text = label,
                style = LpTypography.InputLabel
            )
        },
        placeholder = {
            Text(
                text = placeholder,
                style = LpTypography.InputPlaceholder,
                color = ContentTertiary
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone
        ),
        singleLine = true,
        visualTransformation = PrefixTransformation("$countryCode  "),
        textStyle = LpTypography.InputText,
        isError = error != null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BorderSelected,
            unfocusedBorderColor = BorderPrimary,
            errorBorderColor = BorderNegative,
            focusedLabelColor = ContentSecondary,
            unfocusedLabelColor = ContentSecondary,
            errorLabelColor = ContentNegative,
            cursorColor = PayUFinanceColors.Primary,
            focusedTextColor = ContentPrimary,
            unfocusedTextColor = ContentPrimary
        ),
        shape = RoundedCornerShape(Spacing30)
    )
}

/**
 * Visual transformation to add +91 prefix
 */
class PrefixTransformation(private val prefix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = AnnotatedString(prefix + text.text),
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset + prefix.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return (offset - prefix.length).coerceAtLeast(0)
                }
            }
        )
    }
}

// Preview Composable
@Preview(showBackground = true, name = "Mobile Input Screen Preview")
@Composable
private fun MobileInputScreenPreview() {
    MobileInputField(
        mobileNumber = "9876543210",
        error = null,
        label = stringResource(R.string.mobile_input_label),
        placeholder = stringResource(R.string.mobile_input_placeholder),
        countryCode = "+91",
        onMobileNumberChanged = {},
        modifier = Modifier.fillMaxWidth()
    )
}

