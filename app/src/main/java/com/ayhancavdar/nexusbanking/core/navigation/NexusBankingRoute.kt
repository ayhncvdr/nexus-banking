/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.navigation

import com.ayhancavdar.nexusbanking.features.filter.navigation.FilterParametersNavType
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import kotlin.reflect.typeOf
import kotlinx.serialization.Serializable

@Serializable
sealed interface NexusBankingRoute {
    @Serializable
    data object Splash : NexusBankingRoute

    @Serializable
    data object Login : NexusBankingRoute

    @Serializable
    data class Otp(val customerName: String, val starredSmsNumber: String) : NexusBankingRoute

    @Serializable
    data class Accounts(val customerName: String) : NexusBankingRoute

    @Serializable
    data class Filter(val filterParameters: FilterParameters? = null) : NexusBankingRoute {
        companion object {
            val typeMap = mapOf(typeOf<FilterParameters?>() to FilterParametersNavType)
        }
    }

    @Serializable
    data class AccountDetails(val accountIban: String) : NexusBankingRoute
}
