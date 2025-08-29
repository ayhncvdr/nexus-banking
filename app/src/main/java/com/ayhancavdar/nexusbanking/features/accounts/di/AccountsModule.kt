/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.di

import com.ayhancavdar.nexusbanking.features.accounts.data.repository.AccountsRepositoryImpl
import com.ayhancavdar.nexusbanking.features.accounts.domain.repository.AccountsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AccountsModule {

    @Binds
    @ViewModelScoped
    abstract fun bindAccountsRepository(
        accountsRepositoryImpl: AccountsRepositoryImpl
    ): AccountsRepository
}
