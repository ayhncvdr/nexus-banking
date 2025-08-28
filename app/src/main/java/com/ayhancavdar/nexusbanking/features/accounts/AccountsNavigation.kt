/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute

internal fun NavGraphBuilder.accounts() {
    composable<NexusBankingRoute.Accounts> {
        AccountsScreen()
    }
}
