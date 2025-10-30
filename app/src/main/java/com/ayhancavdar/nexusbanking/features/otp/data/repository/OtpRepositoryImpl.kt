/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.data.repository

import com.ayhancavdar.nexusbanking.core.network.AuthApiService
import com.ayhancavdar.nexusbanking.features.otp.data.model.OtpRequest
import com.ayhancavdar.nexusbanking.features.otp.data.model.OtpResponse
import com.ayhancavdar.nexusbanking.features.otp.domain.repository.OtpRepository
import javax.inject.Inject

class OtpRepositoryImpl @Inject constructor(
    private val autApiService: AuthApiService,
) : OtpRepository {
    override suspend fun otp(otpPassword: String): Result<OtpResponse> {
        val otpRequest = OtpRequest(otpPassword)
        return autApiService.otp(otpRequest)
    }
}
