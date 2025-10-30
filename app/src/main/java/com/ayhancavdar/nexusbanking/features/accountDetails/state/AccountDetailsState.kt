/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails.state

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class AccountDetailsState(
    val accountIban: String = "",
    val accountName: String = "",
    val accountBalance: String = "",
    val accountCurrency: String = "",
    val accountNumber: String = "",
    val accountBranchName: String = "",
    val accountBranchCode: String = "",
    val customerName: String = ""
) : Parcelable
