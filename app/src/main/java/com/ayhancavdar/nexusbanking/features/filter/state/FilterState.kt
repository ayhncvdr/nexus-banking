/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter.state

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import java.util.Calendar
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class FilterState(
    val selectedCurrency: CurrencyFilter = CurrencyFilter.All,
    val startDateMillis: Long = getDefaultStartDateMillis(),
    val endDateMillis: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
) : Parcelable {
    companion object {
        fun getDefaultStartDateMillis(): Long {
            val calendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
                set(Calendar.YEAR, 2010)
                set(Calendar.MONTH, Calendar.JANUARY)
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return calendar.timeInMillis
        }
    }
}

@Parcelize
data class FilterParameters(
    val selectedCurrency: CurrencyFilter = CurrencyFilter.All,
    val startDateMillis: Long = FilterState.getDefaultStartDateMillis(),
    val endDateMillis: Long = System.currentTimeMillis(),
) : Parcelable
