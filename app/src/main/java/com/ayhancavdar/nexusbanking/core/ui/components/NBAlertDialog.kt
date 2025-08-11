/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun NBAlertDialog(
    onDismissRequest: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    confirmButtonOnClick: (() -> Unit)? = null,
    @StringRes confirmButtonText: Int? = null,
    dismissButtonOnClick: (() -> Unit)? = null,
    @StringRes dismissButtonText: Int? = null,
    @StringRes title: Int? = null,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = title?.let { { Text(text = stringResource(id = it)) } },
        text = {
            Text(text = text)
        },
        confirmButton = {
            confirmButtonText?.let { buttonTextRes ->
                TextButton(
                    onClick = {
                        confirmButtonOnClick?.invoke()
                        onDismissRequest()
                    }
                ) {
                    Text(stringResource(id = buttonTextRes))
                }
            }
        },
        dismissButton = {
            dismissButtonText?.let { buttonTextRes ->
                TextButton(
                    onClick = {
                        dismissButtonOnClick?.invoke()
                        onDismissRequest()
                    }
                ) {
                    Text(stringResource(id = buttonTextRes))
                }
            }
        }
    )
}
