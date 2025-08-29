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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme

@Composable
fun AccountDetailsScreen(accountIban: String) {
    // TODO: Implement AccountDetailsScreen
    Scaffold { innerPadding ->
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
                text = "Iban: $accountIban",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x8))
            )
        }
    }
}

@Preview
@Composable
private fun AccountDetailsScreenPreview() {
    NexusBankingTheme {
        AccountDetailsScreen(
            accountIban = "TR123456789012345678901234"
        )
    }
}
