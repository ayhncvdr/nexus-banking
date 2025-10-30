/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.domain.repository

import com.ayhancavdar.nexusbanking.features.otp.data.model.OtpResponse

interface OtpRepository {
    suspend fun otp(otpPassword: String): Result<OtpResponse>
}
