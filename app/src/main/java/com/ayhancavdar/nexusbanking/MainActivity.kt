/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ayhancavdar.nexusbanking.core.navigation.AppNavHost
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.core.ui.components.AppBackground
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusBankingTheme {
                val navController = rememberNavController()
                AppBackground {
                    AppNavHost(
                        modifier = Modifier,
                        navController = navController,
                        startDestination = NexusBankingRoute.Splash
                    )
                }
            }
        }
    }
}
