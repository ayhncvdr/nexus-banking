/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.domain.repository

import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import com.ayhancavdar.nexusbanking.features.login.domain.model.User

interface LoginRepository {
    suspend fun login(credentials: Credentials): Result<User>
}
