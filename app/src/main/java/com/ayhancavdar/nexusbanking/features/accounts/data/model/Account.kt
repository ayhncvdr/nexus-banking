/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.accounts.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Account(
    @SerialName("id") val id: String? = null,
    @SerialName("currency") val currency: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("iban") val iban: String? = null,
    @SerialName("no") val no: String? = null,
    @SerialName("branchCode") val branchCode: String? = null,
    @SerialName("branchName") val branchName: String? = null,
    @SerialName("extensionNo") val extensionNo: String? = null,
    @SerialName("currentBalance") val currentBalance: String? = null,
    @SerialName("availableBalance") val availableBalance: String? = null,
    @SerialName("flexibleAccountLimit") val flexibleAccountLimit: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("openingDate") val openingDate: String? = null,
    @SerialName("additionalInfo") val additionalInfo: Map<String, String>? = null
) : Parcelable
