/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.ayhancavdar.nexusbanking.features.filter.state.FilterParameters
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val sharedJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

object FilterParametersNavType : NavType<FilterParameters?>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): FilterParameters? {
        val json = bundle.getString(key)
        return if (json == null || json == "null") {
            null
        } else {
            try {
                sharedJson.decodeFromString<FilterParameters>(json)
            } catch (e: SerializationException) {
                null
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    override fun parseValue(value: String): FilterParameters? {
        return if (value == "null") {
            null
        } else {
            try {
                sharedJson.decodeFromString<FilterParameters>(value)
            } catch (e: SerializationException) {
                null
            } catch (e: IllegalArgumentException) {
                null
            }
        }
    }

    override fun serializeAsValue(value: FilterParameters?): String {
        return if (value == null) "null" else sharedJson.encodeToString(value)
    }

    override fun put(bundle: Bundle, key: String, value: FilterParameters?) {
        val json = if (value == null) "null" else sharedJson.encodeToString(value)
        bundle.putString(key, json)
    }
}
