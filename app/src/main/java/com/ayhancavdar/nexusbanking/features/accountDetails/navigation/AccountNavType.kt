/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accountDetails.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.ayhancavdar.nexusbanking.features.accounts.data.model.Account
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val sharedJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

object AccountNavType : NavType<Account>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Account? {
        return bundle.getString(key)?.let { sharedJson.decodeFromString(it) }
    }

    override fun parseValue(value: String): Account {
        return sharedJson.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: Account) {
        bundle.putString(key, sharedJson.encodeToString(value))
    }

    override fun serializeAsValue(value: Account): String {
        return Uri.encode(sharedJson.encodeToString(value))
    }
}
