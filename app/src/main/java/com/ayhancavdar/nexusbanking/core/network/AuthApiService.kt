/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.network

import com.ayhancavdar.nexusbanking.features.login.data.model.LoginRequest
import com.ayhancavdar.nexusbanking.features.login.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Result<LoginResponse>
}
