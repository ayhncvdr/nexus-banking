/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters

internal fun NavGraphBuilder.filter(navController: NavController) {
    composable<NexusBankingRoute.Filter> {
        val initialFilterParameters = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<FilterParameters>("currentFilterParameters")

        FilterScreen(
            initialFilterParameters = initialFilterParameters,
            onNavigateBack = {
                navController.popBackStack()
            },
            onApplyFilter = { filterParameters ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("filterParameters", filterParameters)
                navController.popBackStack()
            }
        )
    }
}
