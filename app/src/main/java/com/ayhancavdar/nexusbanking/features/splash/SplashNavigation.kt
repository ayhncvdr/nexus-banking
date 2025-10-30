/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.splash

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute

private const val ANIMATION_DURATION = 300

internal fun NavGraphBuilder.splash(navController: NavController) {
    composable<NexusBankingRoute.Splash>(
        exitTransition = {
            fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
        }
    ) {
        SplashScreen(
            onSplashFinish = {
                navController.navigate(NexusBankingRoute.Login) {
                    popUpTo<NexusBankingRoute.Splash> {
                        inclusive = true
                    }
                }
            }
        )
    }
}
