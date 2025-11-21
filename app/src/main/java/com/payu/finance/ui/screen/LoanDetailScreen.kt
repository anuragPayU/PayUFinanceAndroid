package com.payu.finance.ui.screen

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazypay.android.elevate.component.ButtonState
import com.lazypay.android.elevate.component.LPButton
import com.lazypay.android.elevate.component.Text as ElevateText
import com.lazypay.android.elevate.theme.*
import androidx.compose.material.ButtonDefaults
import com.payu.finance.common.Resource
import com.payu.finance.ui.components.StatusChip
import com.payu.finance.ui.model.*
import com.payu.finance.ui.theme.PayUFinanceColors
import com.payu.finance.ui.viewmodel.LoanDetailViewModel

/**
 * Loan Detail Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanDetailScreen(
    viewModel: LoanDetailViewModel,
    loanId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(loanId) {
        viewModel.loadLoanDetails(loanId)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp), // Disable automatic window insets for edge-to-edge
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { 
                    ElevateText(
                        markup = "Loan Details",
                        style = LpTypography.TitleSection,
                        color = PayUFinanceColors.ContentPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack, 
                            contentDescription = "Back",
                            tint = PayUFinanceColors.ContentPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PayUFinanceColors.BackgroundPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(PayUFinanceColors.BackgroundPrimary)
                .statusBarsPadding() // Only add status bar padding
                .padding(paddingValues) // Keep Scaffold padding for TopAppBar
        ) {
            when (val resource = uiState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Error -> {
                    ErrorView(
                        message = resource.message ?: "An error occurred",
                        onRetry = { viewModel.loadLoanDetails(loanId) }
                    )
                }
                is Resource.Success -> {
                    val loanDetail = resource.data
                    if (loanDetail != null) {
                        LoanDetailContent(
                            loanDetail = loanDetail,
                            onDownloadClick = { url, fileName ->
                            if (downloadFile(context, url, fileName)) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Download started: $fileName")
                                }
                            } else {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Failed to start download")
                                }
                            }
                        },
                            onActionClick = { action ->
                                when (action.type) {
                                    "REPAYMENT" -> {
                                        // Handle repayment action
                                coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Opening repayment...")
                                        }
                                }
                                    "SEE_SECHEDULE" -> {
                                        // Handle see schedule action
                                coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Opening schedule...")
                                        }
                                    }
                                    else -> {
                                        // Handle other actions
                                }
                            }
                        }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Loan Detail Content
 */
@Composable
fun LoanDetailContent(
    loanDetail: LoanDetailUiState,
    onDownloadClick: (String, String) -> Unit,
    onActionClick: (ActionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = Spacing40,
            vertical = Spacing20
        ),
        verticalArrangement = Arrangement.spacedBy(Spacing30)
    ) {
        items(
            count = loanDetail.sections.size,
            key = { index -> index }
        ) { index ->
            when (val section = loanDetail.sections[index]) {
                is LoanDetailSectionUiItem.DetailCard -> {
                    DetailCardSection(section = section)
                }
                is LoanDetailSectionUiItem.EmiDetail -> {
                    EmiDetailSection(
                        section = section,
                        onActionClick = onActionClick
                    )
                }
                is LoanDetailSectionUiItem.AutoPayStatus -> {
                    AutoPayStatusSection(section = section)
                }
                is LoanDetailSectionUiItem.ForeclosureCard -> {
                    ForeclosureCardSection(
                        section = section,
                        onActionClick = onActionClick
                    )
                }
                is LoanDetailSectionUiItem.RowListCard -> {
                    RowListCardSection(
                        section = section,
                        onDownloadClick = onDownloadClick
                    )
                }
            }
        }
    }
}

/**
 * Detail Card Section
 */
@Composable
fun DetailCardSection(
    section: LoanDetailSectionUiItem.DetailCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            verticalArrangement = Arrangement.spacedBy(Spacing20)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ElevateText(
                        markup = section.subtitle ?: "",
                        style = LpTypography.BodySmall,
                        color = PayUFinanceColors.ContentSecondary,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    ElevateText(
                        markup = section.title,
                        style = LpTypography.TitleSection,
                        color = PayUFinanceColors.ContentPrimary
                    )
                }
                section.statusLabel?.let { label ->
                    StatusChip(
                        status = when (label.uppercase()) {
                            "ACTIVE", "PENDING" -> EmiStatus.PENDING
                            "PAID", "COMPLETED" -> EmiStatus.PAID
                            "OVERDUE" -> EmiStatus.OVERDUE
                            else -> EmiStatus.PENDING
                        }
                    )
                }
            }
        }
    }
}

/**
 * EMI Detail Section
 */
@Composable
fun EmiDetailSection(
    section: LoanDetailSectionUiItem.EmiDetail,
    onActionClick: (ActionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing20)
    ) {
        ElevateText(
            markup = section.title,
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing20)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = PayUFinanceColors.BorderPrimary,
                    shape = RoundedCornerShape(Spacing30)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(Spacing30),
            colors = CardDefaults.cardColors(
                containerColor = PayUFinanceColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing40),
                verticalArrangement = Arrangement.spacedBy(Spacing30)
            ) {
                // Header
                section.header?.let { header ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing20)
                    ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                ElevateText(
                                    markup = header.title,
                                    style = LpTypography.TitlePrimary,
                                    color = PayUFinanceColors.ContentPrimary,
                                    modifier = Modifier.padding(bottom = Spacing10)
                                )
                                header.subtitle?.let { subtitle ->
                                    ElevateText(
                                        markup = subtitle,
                                        style = LpTypography.BodyNormal,
                                        color = PayUFinanceColors.ContentSecondary
                                    )
                                }
                            }
                            header.percentage?.let { percentage ->
                                ElevateText(
                                    markup = "$percentage%",
                                    style = LpTypography.TitleSecondary,
                                    color = PayUFinanceColors.Primary
                                )
                            }
                        }
                        header.percentage?.let { percentage ->
                            LinearProgressIndicator(
                                progress = (percentage.toFloatOrNull() ?: 0f) / 100f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = PayUFinanceColors.ProgressBarFill,
                                trackColor = PayUFinanceColors.ProgressBarTrack
                            )
                        }
                    }
                    Divider(
                        color = PayUFinanceColors.BorderPrimary,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = Spacing10)
                    )
                }

                // Rows
                section.rows.forEachIndexed { index, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Spacing10),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevateText(
                            markup = row.title,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentSecondary,
                            modifier = Modifier.weight(1f)
                        )
                        ElevateText(
                            markup = row.subtitle,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentPrimary,
                            modifier = Modifier.padding(start = Spacing20)
                        )
                    }
                    if (index < section.rows.size - 1) {
                        Divider(
                            color = PayUFinanceColors.BorderPrimary,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = Spacing10)
                        )
                    }
                }

                // Primary Action
                section.primaryAction?.let { action ->
                    Spacer(modifier = Modifier.height(Spacing10))
                    TextButton(
                        onClick = { onActionClick(action) },
                modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        ElevateText(
                            markup = action.text ?: "",
                            style = LpTypography.TitleSecondary,
                            color = PayUFinanceColors.Primary
                        )
                        Spacer(modifier = Modifier.width(Spacing10))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = PayUFinanceColors.Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Auto Pay Status Section
 */
@Composable
fun AutoPayStatusSection(
    section: LoanDetailSectionUiItem.AutoPayStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing20)
    ) {
        ElevateText(
            markup = section.title,
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing20)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = PayUFinanceColors.BorderPrimary,
                    shape = RoundedCornerShape(Spacing30)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(Spacing30),
            colors = CardDefaults.cardColors(
                containerColor = PayUFinanceColors.SuccessBackground
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing40),
                horizontalArrangement = Arrangement.spacedBy(Spacing20),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ElevateText(
                        markup = section.statusCard.title,
                        style = LpTypography.TitlePrimary,
                        color = PayUFinanceColors.Success,
                        modifier = Modifier.padding(bottom = Spacing10)
                    )
                    section.statusCard.subtitle?.let { subtitle ->
                        ElevateText(
                            markup = subtitle,
                            style = LpTypography.BodyNormal,
                            color = PayUFinanceColors.ContentSecondary
                        )
                    }
                }
            }
        }
    }
}

/**
 * Foreclosure Card Section
 */
@Composable
fun ForeclosureCardSection(
    section: LoanDetailSectionUiItem.ForeclosureCard,
    onActionClick: (ActionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing20)
    ) {
        ElevateText(
            markup = section.title,
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing20)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = PayUFinanceColors.BorderPrimary,
                    shape = RoundedCornerShape(Spacing30)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(Spacing30),
            colors = CardDefaults.cardColors(
                containerColor = PayUFinanceColors.CardBackground
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing40),
                verticalArrangement = Arrangement.spacedBy(Spacing20)
            ) {
                ElevateText(
                    markup = section.card.title,
                    style = LpTypography.BodySmall,
                    color = PayUFinanceColors.ContentSecondary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                ElevateText(
                    markup = section.card.subtitle,
                    style = LpTypography.TitleSection,
                    color = PayUFinanceColors.ContentPrimary,
                    modifier = Modifier.padding(bottom = Spacing10)
                )
                section.card.description?.let { description ->
                    ElevateText(
                        markup = description,
                        style = LpTypography.BodyNormal,
                        color = PayUFinanceColors.ContentSecondary,
                        modifier = Modifier.padding(bottom = Spacing20)
                    )
                }
                section.card.action?.let { action ->
                    LPButton(
                        text = action.text ?: "",
                        state = ButtonState.Enabled,
                        backgroundColor = PayUFinanceColors.Primary,
                        pressedBackgroundColor = PayUFinanceColors.PrimaryDark,
                        disabledBackgroundColor = PayUFinanceColors.BackgroundTertiary,
                        contentColor = PayUFinanceColors.BackgroundPrimary,
                        pressedContentColor = PayUFinanceColors.BackgroundPrimary,
                        disabledContentColor = PayUFinanceColors.ContentTertiary,
                        circularProgressColor = PayUFinanceColors.BackgroundPrimary,
                        buttonElevation = ButtonDefaults.elevation(),
                        onClick = { onActionClick(action) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Row List Card Section
 */
@Composable
fun RowListCardSection(
    section: LoanDetailSectionUiItem.RowListCard,
    onDownloadClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing20)
    ) {
        ElevateText(
            markup = section.title,
            style = LpTypography.TitleSection,
            color = PayUFinanceColors.ContentPrimary,
            modifier = Modifier.padding(bottom = Spacing10)
        )

        section.items.forEach { item ->
            DocumentDownloadCard(
                title = item.title,
                onDownloadClick = {
                    item.action?.url?.let { url ->
                        val fileName = "${item.title.replace(" ", "_")}.pdf"
                        onDownloadClick(url, fileName)
                    }
                }
            )
        }
    }
}

/**
 * Document Download Card
 */
@Composable
fun DocumentDownloadCard(
    title: String,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = PayUFinanceColors.BorderPrimary,
                shape = RoundedCornerShape(Spacing30)
            )
            .clickable(onClick = onDownloadClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(Spacing30),
        colors = CardDefaults.cardColors(
            containerColor = PayUFinanceColors.CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing40),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevateText(
                markup = title,
                style = LpTypography.BodyNormal,
                color = PayUFinanceColors.ContentPrimary
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Download",
                tint = PayUFinanceColors.Primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Info Item Component
 */
@Composable
fun InfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ElevateText(
            markup = label,
            style = LpTypography.BodySmall,
            color = PayUFinanceColors.ContentSecondary,
            modifier = Modifier.padding(bottom = Spacing10)
        )
        ElevateText(
            markup = value,
            style = LpTypography.BodyNormal,
            color = PayUFinanceColors.ContentPrimary
        )
    }
}

/**
 * Download File Function
 * Returns true if download was successfully started, false otherwise
 */
fun downloadFile(context: Context, url: String, fileName: String): Boolean {
    return try {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as? DownloadManager
        if (downloadManager == null) {
            false
        } else {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Downloading $fileName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setMimeType("application/pdf")

            downloadManager.enqueue(request)
            true
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

// Preview Composable
@Preview(showBackground = true, name = "Loan Detail Screen Preview")
@Composable
private fun LoanDetailScreenPreview() {
    com.payu.finance.ui.theme.PayUFinanceTheme {
        val mockLoanDetail = LoanDetailUiState(
            sections = listOf(
                LoanDetailSectionUiItem.DetailCard(
                    title = "₹4,00,000 Loan",
                    subtitle = "Loan Details",
                    statusLabel = "Active",
                    statusColor = "#10B981"
                ),
                LoanDetailSectionUiItem.EmiDetail(
                    title = "EMI details",
                    header = EmiDetailHeader(
                        title = "3 EMIs remaining",
                        subtitle = "₹12,100 left to pay",
                        percentage = "80"
                    ),
                    rows = listOf(
                        EmiDetailRow("Loan amount", "₹10,000"),
                        EmiDetailRow("EMI amount", "₹2,000/m"),
                        EmiDetailRow("Tenure", "3 months")
                    ),
                    primaryAction = ActionItem("See full EMI schedule", "SEE_SECHEDULE", "")
                ),
                LoanDetailSectionUiItem.AutoPayStatus(
                    title = "Auto-pay",
                    statusCard = AutoPayStatusCard(
                        title = "Auto-pay active",
                        subtitle = "Sit back and relax! Your bill will be auto-paid"
                    )
                ),
                LoanDetailSectionUiItem.RowListCard(
                    title = "Actions",
                    items = listOf(
                        ActionableCardItem(
                            title = "Loan Agreement",
                            action = ActionItem("", "DOWNLOADABLE", "https://example.com/loan_agreement.pdf")
                        ),
                        ActionableCardItem(
                            title = "Sanction letter",
                            action = ActionItem("", "DOWNLOADABLE", "https://example.com/sanction_letter.pdf")
                        )
                    )
                )
            )
        )
        
        LoanDetailContent(
            loanDetail = mockLoanDetail,
            onDownloadClick = { _, _ -> },
            onActionClick = {}
        )
    }
}

