/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.accounts.presentation.AccountsScreen
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters

internal fun NavGraphBuilder.accounts(navController: NavController) {
    composable<NexusBankingRoute.Accounts> { backStackEntry ->
        val filterResult = backStackEntry.savedStateHandle
            .getStateFlow<FilterParameters?>("filterParameters", null)
            .collectAsState()

        AccountsScreen(
            filterResult = filterResult.value,
            onNavigateToLogin = {
                navController.navigate(NexusBankingRoute.Login) {
                    popUpTo<NexusBankingRoute.Accounts> {
                        inclusive = true
                    }
                }
            },
            onNavigateToFilter = { currentFilters ->
                navController.navigate(NexusBankingRoute.Filter(filterParameters = currentFilters))
            },
            onNavigateToAccountDetails = { account ->
                navController.navigate(NexusBankingRoute.AccountDetails(accountIban = account.iban.orEmpty()))
            }
        )
    }
}
