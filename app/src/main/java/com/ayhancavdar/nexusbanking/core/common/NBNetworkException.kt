/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.common

import java.io.IOException

data class NBNetworkException(
    val errorCode: Int = DEFAULT_ERROR_CODE,
    val errorMessage: String? = null,
    val errorTitle: String? = null,
) : IOException() {
    companion object {
        private const val DEFAULT_ERROR_CODE: Int = -1

        fun BaseResponse.toNBNetworkException(): NBNetworkException {
            return NBNetworkException(
                errorCode = errorId ?: DEFAULT_ERROR_CODE,
                errorMessage = errorMsg,
                errorTitle = title,
            )
        }
    }
}
