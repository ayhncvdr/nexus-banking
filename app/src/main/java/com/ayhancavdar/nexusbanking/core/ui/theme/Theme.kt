/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppDarkColorScheme = darkColorScheme(
    primary = NBColors.primaryGreen,
    onPrimary = NBColors.pureWhite,
    primaryContainer = NBColors.primaryGreenDark,
    onPrimaryContainer = NBColors.primaryGreenLight,
    secondary = NBColors.primaryForestGreen,
    onSecondary = NBColors.pureWhite,
    secondaryContainer = NBColors.primaryForestGreen.copy(alpha = 0.3f),
    onSecondaryContainer = NBColors.pureWhite,
    tertiary = NBColors.primaryMint,
    onTertiary = NBColors.primaryGreenDark,
    tertiaryContainer = NBColors.primaryEmerald,
    onTertiaryContainer = NBColors.pureWhite,
    error = NBColors.secondaryAlert,
    onError = NBColors.pureWhite,
    errorContainer = NBColors.secondaryAlert.copy(alpha = 0.3f),
    onErrorContainer = NBColors.pureWhite,
    background = NBColors.primaryGreenDark,
    onBackground = NBColors.pureWhite,
    surface = NBColors.charcoalGrey,
    onSurface = NBColors.pureWhite,
    surfaceVariant = NBColors.darkGrey,
    onSurfaceVariant = NBColors.lightGrey,
    outline = NBColors.mediumGrey,
    inverseOnSurface = NBColors.primaryGreenDark,
    inverseSurface = NBColors.offWhite,
    inversePrimary = NBColors.primaryGreenLight,
    surfaceTint = NBColors.primaryGreen,
    outlineVariant = NBColors.darkGrey,
    scrim = Color.Black.copy(alpha = 0.3f),
)

private val AppLightColorScheme = lightColorScheme(
    primary = NBColors.primaryGreen,
    onPrimary = NBColors.pureWhite,
    primaryContainer = NBColors.primaryGreenLight,
    onPrimaryContainer = NBColors.primaryGreenDark,
    secondary = NBColors.primaryForestGreen,
    onSecondary = NBColors.pureWhite,
    secondaryContainer = NBColors.primaryForestGreen.copy(alpha = 0.2f),
    onSecondaryContainer = NBColors.primaryGreenDark,
    tertiary = NBColors.primaryEmerald,
    onTertiary = NBColors.pureWhite,
    tertiaryContainer = NBColors.primaryMint,
    onTertiaryContainer = NBColors.primaryGreenDark,
    error = NBColors.secondaryAlert,
    onError = NBColors.pureWhite,
    errorContainer = NBColors.secondaryAlert.copy(alpha = 0.2f),
    onErrorContainer = NBColors.secondaryAlert,
    background = NBColors.offWhite,
    onBackground = NBColors.nearBlack,
    surface = NBColors.pureWhite,
    onSurface = NBColors.nearBlack,
    surfaceVariant = NBColors.lightGrey,
    onSurfaceVariant = NBColors.charcoalGrey,
    outline = NBColors.mediumGrey,
    inverseOnSurface = NBColors.offWhite,
    inverseSurface = NBColors.primaryGreenDark,
    inversePrimary = NBColors.primaryGreenLight,
    surfaceTint = NBColors.primaryGreen,
    outlineVariant = NBColors.lightGrey,
    scrim = Color.Black.copy(alpha = 0.3f),
)

@Composable
fun NexusBankingTheme(
    useDarkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) {
        AppDarkColorScheme
    } else {
        AppLightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
