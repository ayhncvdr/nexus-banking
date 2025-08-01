/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.ui.theme

import androidx.compose.ui.graphics.Color

object NBColors {
    // Primary colors
    val primaryGreen = Color(0xFF2E7D32)
    val primaryGreenLight = Color(0xFF4CAF50)
    val primaryGreenDark = Color(0xFF1B5E20)
    val primaryForestGreen = Color(0xFF388E3C)
    val primaryEmerald = Color(0xFF00C853)
    val primaryMint = Color(0xFF81C784)

    // Greys, whites, and blacks
    val pureWhite = Color(0xFFFFFFFF)
    val offWhite = Color(0xFFFAFAFA)
    val lightGrey = Color(0xFFF5F5F5)
    val mediumGrey = Color(0xFFE0E0E0)
    val darkGrey = Color(0xFF757575)
    val charcoalGrey = Color(0xFF424242)
    val nearBlack = Color(0xFF212121)
    val pureBlack = Color(0xFF000000)

    // Secondary colors
    val secondarySuccess = Color(0xFF4CAF50)
    val secondaryCaution = Color(0xFFFF9800)
    val secondaryWarning = Color(0xFFFFC107)
    val secondaryAlert = Color(0xFFE53935)

    // Gradients
    val buttonGradientStart = Color(0xFF2E7D32)
    val buttonGradientEnd = Color(0xFF4CAF50)
    val backgroundGradientStart = Color(0xFF1B5E20)
    val backgroundGradientEnd = Color(0xFF2E7D32)

    // Switch color
    val switchColor = Color(0xFFE0E0E0)

    // Search bar colors
    val searchBarText = Color.White.copy(alpha = 0.9f)
    val searchBarFill = Color(0xFF2E7D32).copy(alpha = 0.1f)

    // ListView colors
    val listViewCard = Color(0x664CAF50)
    val listViewSeparator = Color(0x99388E3C)

    // Shadow color
    val shadowGreen = Color(0xFF2E7D32).copy(alpha = 0.2f)

    // Other common colors
    val white = Color.White
    val black = Color.Black
    val transparent = Color.Transparent
}
