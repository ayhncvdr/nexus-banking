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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NBTypography

@Composable
fun NBTextField(
    onValueChange: (String) -> Unit,
    @StringRes placeholderTextRes: Int,
    value: String,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = NBColors.nearBlack,
        unfocusedTextColor = NBColors.nearBlack,
        cursorColor = NBColors.primaryGreen,
        focusedIndicatorColor = NBColors.primaryGreen,
        unfocusedIndicatorColor = NBColors.mediumGrey,
        focusedLabelColor = NBColors.primaryGreen,
        unfocusedLabelColor = NBColors.darkGrey,
        focusedPlaceholderColor = NBColors.darkGrey,
        unfocusedPlaceholderColor = NBColors.darkGrey,
        focusedContainerColor = NBColors.offWhite,
        unfocusedContainerColor = NBColors.offWhite,
        disabledContainerColor = NBColors.lightGrey,
        errorContainerColor = NBColors.offWhite,
        errorTextColor = NBColors.nearBlack,
        errorIndicatorColor = NBColors.secondaryAlert,
        errorLabelColor = NBColors.secondaryAlert,
        errorCursorColor = NBColors.primaryGreen,
    ),
    @StringRes errorTextRes: Int? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    textStyle: TextStyle = NBTypography.titleSeventeenNormal,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(stringResource(id = placeholderTextRes)) },
        singleLine = singleLine,
        isError = isError,
        supportingText = {
            if (isError && errorTextRes != null) {
                Text(
                    text = stringResource(id = errorTextRes),
                    color = NBColors.secondaryAlert,
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        colors = colors,
        textStyle = textStyle
    )
}
