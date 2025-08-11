/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.data.mapper

import com.ayhancavdar.nexusbanking.features.login.data.model.LoginRequest
import com.ayhancavdar.nexusbanking.features.login.data.model.LoginResponse
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import com.ayhancavdar.nexusbanking.features.login.domain.model.User

fun Credentials.toRequest(): LoginRequest {
    return LoginRequest(
        username = this.username,
        password = this.password
    )
}

fun LoginResponse.toDomain(): User? {
    val name = this.customerName
    val smsOtpNumbers = this.smsOtpNumbers

    return if (name != null && smsOtpNumbers != null) {
        User(
            customerName = name,
            smsOtpNumbers = smsOtpNumbers,
        )
    } else {
        null
    }
}
