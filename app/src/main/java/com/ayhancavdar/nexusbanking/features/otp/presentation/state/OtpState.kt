/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class OtpState(
    val otpInput: String = "",
    val otpError: Int? = null,
    val isContinueButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val otpApiError: String? = null,
    val navigateToAccounts: Boolean = false,
    val countdownSeconds: Int = 120,
    val formattedCountdown: String = "02:00",
    val sliderProgress: Float = 1.0f,
    val isTimerRunning: Boolean = false,
    val canResendOtp: Boolean = false,
    val starredSmsNumber: String = ""
) : Parcelable
