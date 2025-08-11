/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class LoginState(
    val usernameInput: String = "",
    val passwordInput: String = "",
    val rememberMe: Boolean = false,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isLoginButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val loginApiError: String? = null,
    val starredSmsNumber: String? = null,
) : Parcelable
