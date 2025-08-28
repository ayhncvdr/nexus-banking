/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.di

import com.ayhancavdar.nexusbanking.features.otp.data.repository.OtpRepositoryImpl
import com.ayhancavdar.nexusbanking.features.otp.domain.repository.OtpRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class OtpModule {
    @Binds
    @ViewModelScoped
    abstract fun bindOtpRepository(
        otpRepositoryImpl: OtpRepositoryImpl
    ): OtpRepository
}
