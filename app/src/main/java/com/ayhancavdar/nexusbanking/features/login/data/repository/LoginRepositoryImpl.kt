/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.data.repository

import com.ayhancavdar.nexusbanking.core.network.AuthApiService
import com.ayhancavdar.nexusbanking.features.login.data.mapper.toDomain
import com.ayhancavdar.nexusbanking.features.login.data.mapper.toRequest
import com.ayhancavdar.nexusbanking.features.login.data.model.LoginResponse
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import com.ayhancavdar.nexusbanking.features.login.domain.model.User
import com.ayhancavdar.nexusbanking.features.login.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
) : LoginRepository {

    override suspend fun login(credentials: Credentials): Result<User> {
        val loginRequest = credentials.toRequest()
        val response: Result<LoginResponse> = authApiService.login(loginRequest)
        return response.mapCatching {
            it.toDomain() ?: throw IllegalArgumentException("Login successful but user data missing in response")
        }
    }
}
