/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.accountDetails.state.AccountDetailsState

@Composable
fun AccountDetailsScreen(
    viewModel: AccountDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountDetailsScreenContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
    )
}

@Composable
private fun AccountDetailsScreenContent(
    uiState: AccountDetailsState,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AccountDetailsAppBar(
                onNavigateBack = onNavigateBack,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NBColors.primaryGreen.copy(alpha = 0.05f),
                            NBColors.offWhite,
                            NBColors.primaryGreen.copy(alpha = 0.02f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(id = R.dimen.x24)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "NexusBanking Logo",
                    modifier = Modifier
                        .height(dimensionResource(id = R.dimen.x128))
                        .aspectRatio(1f)
                        .shadow(
                            elevation = dimensionResource(id = R.dimen.x4),
                            shape = CircleShape,
                            clip = false
                        )
                        .clip(CircleShape)
                        .background(Color.White),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))

                AccountDetailsHeaderSection(
                    customerName = uiState.customerName,
                    accountBalance = uiState.accountBalance,
                    accountCurrency = uiState.accountCurrency
                )

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x40)))

                AccountDetailsCard(uiState = uiState)

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x24)))
            }
        }
    }
}

@Composable
private fun AccountDetailsHeaderSection(
    customerName: String,
    accountBalance: String,
    accountCurrency: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = customerName,
            style = MaterialTheme.typography.headlineMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

        Text(
            text = stringResource(id = R.string.accountDetails_availableBalance_label_text),
            style = MaterialTheme.typography.bodyLarge,
            color = NBColors.primaryGreenLight,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x4)))

        Text(
            text = "$accountBalance $accountCurrency",
            style = MaterialTheme.typography.headlineSmall,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AccountDetailsCard(
    uiState: AccountDetailsState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensionResource(id = R.dimen.x8),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
                clip = false
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.x24))
        ) {
            AccountDetailItem(
                label = stringResource(id = R.string.accountDetails_accountName_label_text),
                value = uiState.accountName
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x16)))

            AccountDetailItemWithShare(
                label = stringResource(id = R.string.accountDetails_iban_label_text),
                value = uiState.accountIban
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x16)))

            AccountDetailItem(
                label = stringResource(id = R.string.accountDetails_accountNumber_label_text),
                value = uiState.accountNumber
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x16)))

            AccountDetailItem(
                label = stringResource(id = R.string.accountDetails_branch_label_text),
                value = "${uiState.accountBranchCode} - ${uiState.accountBranchName}"
            )
        }
    }
}

@Composable
private fun AccountDetailItemWithShare(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = NBColors.primaryGreenLight,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    val sendIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, value)
                        type = "text/plain"
                    }
                    val shareIntent = android.content.Intent.createChooser(sendIntent, null)
                    try {
                        context.startActivity(shareIntent)
                    } catch (e: android.content.ActivityNotFoundException) {
                        /*No op*/
                    }
                },
                modifier = Modifier.size(dimensionResource(id = R.dimen.x32))
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_share),
                    contentDescription = "Share IBAN",
                    tint = NBColors.primaryGreen,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x20))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x4)))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun AccountDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = NBColors.primaryGreenLight
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.x4)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailsAppBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24)),
                    tint = NBColors.nearBlack
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = {},
    )
}

@Preview
@Composable
private fun AccountDetailsScreenPreview() {
    NexusBankingTheme {
        AccountDetailsScreenContent(
            uiState = AccountDetailsState(
                accountIban = "TR00 0000 0000 0000 0000 0000 00",
                accountName = "My Account",
                accountBalance = "1000.00",
                accountCurrency = "USD",
                accountNumber = "123456789",
                accountBranchName = "Main Branch",
                accountBranchCode = "001",
                customerName = "John Doe"
            ),
            onNavigateBack = {},
        )
    }
}
