/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import com.ayhancavdar.nexusbanking.features.accounts.presentation.state.AccountsState

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToFilter: () -> Unit = {},
    onNavigateToAccountDetails: (Account) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    AccountsScreenContent(
        uiState = uiState,
        onSearchTextChange = viewModel::onSearchTextChanged,
        onClearSearch = viewModel::onClearSearch,
        onLogoutClick = viewModel::onLogoutClick,
        onLogoutConfirm = {
            viewModel.onLogoutConfirm()
            onNavigateToLogin()
        },
        onLogoutCancel = viewModel::onLogoutCancel,
        onNavigateToFilter = onNavigateToFilter,
        onNavigateToAccountDetails = onNavigateToAccountDetails,
    )
}

@Composable
private fun AccountsScreenContent(
    uiState: AccountsState,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onLogoutClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onLogoutCancel: () -> Unit,
    onNavigateToFilter: () -> Unit,
    onNavigateToAccountDetails: (Account) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            AccountsAppBar(onLogoutClick = onLogoutClick, onFilterClick = onNavigateToFilter)
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
            when {
                uiState.isLoading ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = NBColors.primaryGreen
                        )
                    }

                uiState.accountsApiError.isNullOrEmpty().not() ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.accountsApiError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.x24))
                        )
                    }

                else ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = dimensionResource(id = R.dimen.x24)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

                        // Header Section with Logo
                        AccountsHeaderSection(
                            totalBalance = uiState.totalAvailableBalance,
                            currency = uiState.defaultCurrency
                        )

                        Spacer(Modifier.height(dimensionResource(id = R.dimen.x40)))

                        // Accounts Card
                        AccountsCard(
                            uiState = uiState,
                            onSearchTextChange = onSearchTextChange,
                            onClearSearch = onClearSearch,
                            onNavigateToAccountDetails = onNavigateToAccountDetails
                        )

                        Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))
                    }

            }
        }
    }

    // Logout confirmation dialog
    if (uiState.showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = onLogoutConfirm,
            onCancel = onLogoutCancel
        )
    }
}

@Composable
private fun AccountsHeaderSection(
    totalBalance: String,
    currency: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
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

        Text(
            text = stringResource(id = R.string.accounts_navigationBar_title),
            style = MaterialTheme.typography.headlineMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

        Text(
            text = stringResource(id = R.string.accounts_availableBalance_label_text),
            style = MaterialTheme.typography.bodyLarge,
            color = NBColors.primaryGreenLight,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x4)))

        Text(
            text = "$totalBalance $currency",
            style = MaterialTheme.typography.headlineSmall,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun AccountsCard(
    uiState: AccountsState,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onNavigateToAccountDetails: (Account) -> Unit
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
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.x24))
        ) {
            SearchBar(
                searchText = uiState.searchText,
                onSearchTextChange = onSearchTextChange,
                onClearSearch = onClearSearch,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x16))
            )

            if (uiState.filteredAccounts.isNotEmpty()) {
                AccountsList(
                    accounts = uiState.filteredAccounts,
                    onNavigateToAccountDetails = onNavigateToAccountDetails,
                )
            } else if (uiState.searchText.isNotEmpty()) {
                Text(
                    text = "No accounts found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NBColors.primaryGreenLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.x24))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountsAppBar(
    onLogoutClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = {
            IconButton(onClick = onLogoutClick) {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24)),
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = NBColors.nearBlack
                )
            }
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24)),
                    painter = painterResource(id = R.drawable.filter_alt),
                    contentDescription = null,
                    tint = NBColors.nearBlack
                )
            }
        }
    )
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = null,
        text = {
            Text(
                text = stringResource(id = R.string.accounts_quit_alert_message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(id = R.string.accounts_quit_alert_quitButton_title),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(id = R.string.accounts_quit_alert_cancelButton_title),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val showClearButton = searchText.isNotEmpty()

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.x56)),
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text(
                text = stringResource(id = R.string.accounts_searchAccount_searchBar_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = NBColors.mediumGrey
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = NBColors.mediumGrey
            )
        },
        trailingIcon = {
            if (showClearButton) {
                TextButton(
                    onClick = onClearSearch,
                ) {
                    Text(
                        text = stringResource(id = R.string.accounts_quit_alert_cancelButton_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = NBColors.primaryGreen,
                    )
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NBColors.lightGrey,
            unfocusedContainerColor = NBColors.lightGrey,
            focusedBorderColor = NBColors.primaryGreen,
            unfocusedBorderColor = NBColors.mediumGrey,
            focusedTextColor = NBColors.nearBlack,
            unfocusedTextColor = NBColors.nearBlack,
        ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x12)),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun AccountsList(
    accounts: List<Account>,
    onNavigateToAccountDetails: (Account) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        accounts.forEachIndexed { index, account ->
            AccountListItem(
                account = account,
                onAccountClick = onNavigateToAccountDetails
            )

            if (index < accounts.lastIndex) {
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = NBColors.lightGrey,
                    modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.x8))
                )
            }
        }
    }
}

@Composable
private fun AccountListItem(
    account: Account,
    onAccountClick: (Account) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAccountClick(account) },
        color = Color.Transparent,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.x12)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.name.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = NBColors.nearBlack,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.x4)),
                    text = account.iban.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = NBColors.primaryGreenLight,
                )
            }

            Text(
                text = "${account.availableBalance.orEmpty()} ${account.currency.orEmpty()}",
                style = MaterialTheme.typography.bodyMedium,
                color = NBColors.nearBlack,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun AccountsScreenPreview() {
    NexusBankingTheme {
        AccountsScreenContent(
            uiState = AccountsState(
                accounts = emptyList(),
                filteredAccounts = listOf(
                    Account(
                        id = "1",
                        name = "Main Account",
                        iban = "TR123456789012345678901234",
                        no = "1234567890",
                        availableBalance = "5,000",
                        currency = "TL"
                    ),
                    Account(
                        id = "2",
                        name = "Savings Account",
                        iban = "TR098765432109876543210987",
                        no = "0987654321",
                        availableBalance = "1,637",
                        currency = "TL"
                    )
                ),
                totalAvailableBalance = "6,637",
            ),
            onSearchTextChange = {},
            onClearSearch = {},
            onLogoutClick = {},
            onLogoutConfirm = {},
            onLogoutCancel = {},
            onNavigateToFilter = {},
            onNavigateToAccountDetails = {},
        )
    }
}
