/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.features.accountDetails.navigation.AccountNavType
import com.ayhancavdar.nexusbanking.features.accountDetails.state.AccountDetailsState
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.typeOf
import kotlinx.coroutines.flow.StateFlow

private const val ACCOUNT_DETAILS_STATE_KEY = "account_details_state"

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val accountDetailsArgs: NexusBankingRoute.AccountDetails = savedStateHandle.toRoute(
        typeMap = mapOf(typeOf<Account>() to AccountNavType)
    )

    //region State
    val uiState: StateFlow<AccountDetailsState> = savedStateHandle.getStateFlow(
        ACCOUNT_DETAILS_STATE_KEY,
        AccountDetailsState(
            accountIban = accountDetailsArgs.account.iban.orEmpty(),
            accountName = accountDetailsArgs.account.name.orEmpty(),
            accountBalance = accountDetailsArgs.account.availableBalance.orEmpty(),
            accountCurrency = accountDetailsArgs.account.currency.orEmpty(),
            accountNumber = accountDetailsArgs.account.no.orEmpty(),
            accountBranchName = accountDetailsArgs.account.branchName.orEmpty(),
            accountBranchCode = accountDetailsArgs.account.branchCode.orEmpty(),
            customerName = accountDetailsArgs.customerName,
        )
    )
    //endregion
}
