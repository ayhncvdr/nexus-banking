/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.presentation.state

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class AccountsState(
    val accounts: List<Account> = emptyList(),
    val filteredAccounts: List<Account> = emptyList(),
    val searchText: String = "",
    val totalAvailableBalance: String = "",
    val defaultCurrency: String = "TL",
    val isLoading: Boolean = false,
    val accountsApiError: String? = null,
    val showLogoutDialog: Boolean = false,
) : Parcelable
