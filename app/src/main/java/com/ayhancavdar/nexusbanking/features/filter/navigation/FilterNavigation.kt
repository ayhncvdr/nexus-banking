/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.filter.FilterScreen
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import kotlin.reflect.typeOf

internal fun NavGraphBuilder.filter(navController: NavController) {
    composable<NexusBankingRoute.Filter>(
        typeMap = mapOf(typeOf<FilterParameters?>() to FilterParametersNavType)
    ) { backStackEntry ->
        val filterRoute = backStackEntry.toRoute<NexusBankingRoute.Filter>()

        FilterScreen(
            initialFilterParameters = filterRoute.filterParameters,
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
