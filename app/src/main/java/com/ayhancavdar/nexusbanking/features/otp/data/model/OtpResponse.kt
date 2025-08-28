/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.data.model

import com.ayhancavdar.nexusbanking.core.common.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class OtpResponse(
    @SerialName("success") override val success: Boolean? = null,
    @SerialName("errorId") override val errorId: Int? = null,
    @SerialName("errorMsg") override val errorMsg: String? = null,
    @SerialName("title") override val title: String? = null,
) : BaseResponse
