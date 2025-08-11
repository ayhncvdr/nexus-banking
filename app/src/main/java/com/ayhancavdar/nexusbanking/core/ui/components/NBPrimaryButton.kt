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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors

private const val DISABLED_BUTTON_ALPHA = 0.4f

@Composable
fun NBPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    brush: Brush = Brush.linearGradient(
        colors = listOf(NBColors.buttonGradientEnd, NBColors.buttonGradientStart)
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    enabled: Boolean = true,
    height: Dp = 50.dp,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .height(height)
            .alpha(if (enabled) 1.0f else DISABLED_BUTTON_ALPHA)
            .clip(shape)
            .background(brush = brush),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            enabled = enabled,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            contentPadding = contentPadding,
            content = content
        )
    }
}
