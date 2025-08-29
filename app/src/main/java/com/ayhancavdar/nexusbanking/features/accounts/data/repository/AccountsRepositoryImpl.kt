/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.data.repository

import com.ayhancavdar.nexusbanking.core.network.AccountsApiService
import com.ayhancavdar.nexusbanking.features.accounts.data.model.AccountsResponse
import com.ayhancavdar.nexusbanking.features.accounts.domain.repository.AccountsRepository
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val accountsApiService: AccountsApiService
) : AccountsRepository {

    override suspend fun getAccounts(): Result<AccountsResponse> {
        return accountsApiService.getAccounts()
    }
}
