/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.accountDetails.AccountDetailsScreen
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import kotlin.reflect.typeOf

internal fun NavGraphBuilder.accountDetails(navController: NavController) {
    composable<NexusBankingRoute.AccountDetails>(
        typeMap = mapOf(typeOf<Account>() to AccountNavType)
    ) {
        AccountDetailsScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
