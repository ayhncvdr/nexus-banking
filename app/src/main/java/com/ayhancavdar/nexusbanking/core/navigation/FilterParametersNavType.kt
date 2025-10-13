/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object FilterParametersNavType : NavType<FilterParameters?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): FilterParameters? {
        val json = bundle.getString(key)
        return if (json == null || json.toString() == "null") null else Json.decodeFromString<FilterParameters>(json)
    }

    override fun parseValue(value: String): FilterParameters? {
        return if (value == "null") null else Json.decodeFromString<FilterParameters>(value)
    }

    override fun serializeAsValue(value: FilterParameters?): String {
        return if (value == null) "null" else Json.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: FilterParameters?) {
        val json = if (value == null) "null" else Json.encodeToString(value)
        bundle.putString(key, json)
    }
}
