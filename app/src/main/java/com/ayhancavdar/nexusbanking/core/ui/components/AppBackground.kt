/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            NBColors.backgroundGradientStart,
            NBColors.backgroundGradientEnd
        )
    )
    Box(
        modifier = modifier
            .background(brush = gradientBrush)
            .fillMaxSize()
    ) {
        content()
    }
}
