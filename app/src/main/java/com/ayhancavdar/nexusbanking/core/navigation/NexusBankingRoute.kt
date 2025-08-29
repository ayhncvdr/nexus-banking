/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NexusBankingRoute {
    @Serializable
    data object Splash : NexusBankingRoute

    @Serializable
    data object Login : NexusBankingRoute

    @Serializable
    data class Otp(val starredSmsNumber: String) : NexusBankingRoute

    @Serializable
    data object Accounts : NexusBankingRoute

    @Serializable
    data object Filter : NexusBankingRoute

    @Serializable
    data class AccountDetails(val accountIban: String) : NexusBankingRoute
}
