/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ayhancavdar.nexusbanking.R

val roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_bold, FontWeight.Bold)
)

object NBTypography {
    val headlineTwentyTwoSemibold = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    )

    val titleSeventeenBold = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp
    )

    val titleSeventeenNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp
    )

    val bodyFifteenBold = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp
    )

    val bodyFifteenMedium = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    )

    val bodyFifteenNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp
    )

    val bodyFourteenNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )

    val bodyTwelveNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

    val bodyElevenNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    )

    val bodyTenNormal = TextStyle(
        fontFamily = roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
}

// Set of Material typography styles
val Typography = Typography(
    displayLarge = NBTypography.headlineTwentyTwoSemibold.copy(fontSize = 57.sp),
    displayMedium = NBTypography.headlineTwentyTwoSemibold.copy(fontSize = 45.sp),
    displaySmall = NBTypography.headlineTwentyTwoSemibold.copy(fontSize = 36.sp),
    headlineLarge = NBTypography.headlineTwentyTwoSemibold.copy(fontSize = 32.sp),
    headlineMedium = NBTypography.headlineTwentyTwoSemibold.copy(fontSize = 28.sp),
    headlineSmall = NBTypography.headlineTwentyTwoSemibold,
    titleLarge = NBTypography.titleSeventeenBold.copy(fontSize = 22.sp),
    titleMedium = NBTypography.titleSeventeenBold,
    titleSmall = NBTypography.titleSeventeenNormal.copy(fontSize = 14.sp),
    bodyLarge = NBTypography.bodyFourteenNormal.copy(fontSize = 16.sp),
    bodyMedium = NBTypography.bodyFourteenNormal,
    bodySmall = NBTypography.bodyTwelveNormal,
    labelLarge = NBTypography.bodyFifteenBold,
    labelMedium = NBTypography.bodyElevenNormal,
    labelSmall = NBTypography.bodyTenNormal
)
