/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
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
    // TODO: Implement AccountDetailsScreen
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AccountDetailsAppBar(
                onNavigateBack = onNavigateBack,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(NBColors.backgroundGradientStart, NBColors.backgroundGradientEnd)
                    )
                ),
        ) {
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x16))
            )

            Text(
                text = "Iban: ${uiState.accountIban}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x8))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountDetailsAppBar(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.filter_navigationBar_title).uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24))
                )
            }
        },
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
