/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.filter.state

import android.content.res.Resources
import android.os.Parcelable
import com.ayhancavdar.nexusbanking.R
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class CurrencyFilter : Parcelable {
    @Parcelize
    data object All : CurrencyFilter()

    @Parcelize
    data class Specific(val code: String) : CurrencyFilter()
}

fun CurrencyFilter.label(resources: Resources): String = when (this) {
    CurrencyFilter.All -> resources.getString(R.string.filter_allButton_title)
    is CurrencyFilter.Specific -> code
}
