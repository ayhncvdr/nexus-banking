/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.components.NBAlertDialog
import com.ayhancavdar.nexusbanking.core.ui.components.NBPrimaryButton
import com.ayhancavdar.nexusbanking.core.ui.components.NBTextField
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.login.presentation.state.LoginState
import java.util.Locale

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginScreenContent(
        uiState = uiState,
        onLoginSuccess = onLoginSuccess,
        onUsernameChange = viewModel::onUsernameChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onRememberMeChange = viewModel::onRememberMeChanged,
        onAttemptLogin = viewModel::attemptLogin,
        onDismissLoginError = viewModel::onDismissLoginError,
        onNavigationToOtp = viewModel::onNavigationToOtp
    )
}

@Composable
private fun LoginScreenContent(
    uiState: LoginState,
    onLoginSuccess: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onAttemptLogin: () -> Unit,
    onDismissLoginError: () -> Unit,
    onNavigationToOtp: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NBColors.primaryGreen.copy(alpha = 0.05f),
                            NBColors.offWhite,
                            NBColors.primaryGreen.copy(alpha = 0.02f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = dimensionResource(id = R.dimen.x24)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.x48)))

                // Welcome Section
                WelcomeSection()

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x40)))

                // Login Card
                LoginCard(
                    uiState = uiState,
                    onUsernameChange = onUsernameChange,
                    onPasswordChange = onPasswordChange,
                    onRememberMeChange = onRememberMeChange,
                    onAttemptLogin = onAttemptLogin
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))
            }
        }
    }

    // Keep your existing alert dialog and navigation logic
    uiState.loginApiError?.let { errorMessage ->
        NBAlertDialog(
            onDismissRequest = onDismissLoginError,
            title = R.string.generic_alert_title_error,
            text = errorMessage,
            confirmButtonText = R.string.login_login_alert_okButton_title,
            dismissButtonText = R.string.login_login_alert_cancelButton_title,
            confirmButtonOnClick = onDismissLoginError,
            dismissButtonOnClick = onDismissLoginError,
        )
    }

    uiState.starredSmsNumber.let { starredSmsNumber ->
        if (!starredSmsNumber.isNullOrEmpty()) {
            onLoginSuccess(starredSmsNumber)
            onNavigationToOtp()
        }
    }
}

@Composable
private fun WelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "NexusBanking Logo",
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.x80))
                .shadow(
                    elevation = dimensionResource(id = R.dimen.x4),
                    shape = CircleShape,
                    clip = false
                ),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))

        Text(
            text = stringResource(id = R.string.login_welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            color = NBColors.nearBlack,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

        Text(
            text = stringResource(id = R.string.login_welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = NBColors.primaryGreenLight,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginCard(
    uiState: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onAttemptLogin: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = dimensionResource(id = R.dimen.x8),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
                clip = false
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.x16)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.x24))
        ) {
            Text(
                text = stringResource(id = R.string.login_form_title),
                style = MaterialTheme.typography.titleLarge,
                color = NBColors.nearBlack,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            LoginForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.x20)),
                username = uiState.usernameInput,
                onUsernameChange = onUsernameChange,
                usernameErrorRes = uiState.usernameError,
                password = uiState.passwordInput,
                onPasswordChange = onPasswordChange,
                passwordErrorRes = uiState.passwordError,
            )

            RememberMeSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.x24)),
                checked = uiState.rememberMe,
                onCheckedChange = onRememberMeChange,
            )

            NBPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.x56)),
                onClick = onAttemptLogin,
                content = {
                    Text(
                        stringResource(id = R.string.login_login_button_title).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x12)),
                enabled = uiState.isLoginButtonEnabled,
            )
        }
    }
}

@Composable
private fun LoginForm(
    modifier: Modifier = Modifier,
    onPasswordChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    password: String,
    @StringRes passwordErrorRes: Int?,
    username: String,
    @StringRes usernameErrorRes: Int?,
) {
    Column(
        modifier = modifier,
    ) {
        val isUsernameError = usernameErrorRes != null
        val isPasswordError = passwordErrorRes != null

        NBTextField(
            value = username,
            onValueChange = onUsernameChange,
            placeholderTextRes = R.string.login_username_textfield_placeholder,
            isError = isUsernameError,
            errorTextRes = usernameErrorRes,
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x16)))

        NBTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholderTextRes = R.string.login_password_textfield_placeholder,
            isError = isPasswordError,
            errorTextRes = passwordErrorRes,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
    }
}

@Composable
fun RememberMeSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.login_rememberMe_label_text),
            style = MaterialTheme.typography.bodyMedium,
            color = NBColors.nearBlack,
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NBColors.primaryGreen,
                checkedTrackColor = NBColors.primaryGreenLight.copy(alpha = 0.5f),
                uncheckedThumbColor = NBColors.mediumGrey,
                uncheckedTrackColor = NBColors.lightGrey,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    NexusBankingTheme {
        LoginScreenContent(
            uiState = LoginState(
                usernameInput = "username",
                passwordInput = "12345",
                rememberMe = true,
                isLoginButtonEnabled = true
            ),
            onLoginSuccess = {},
            onUsernameChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            onAttemptLogin = {},
            onDismissLoginError = {},
            onNavigationToOtp = {}
        )
    }
}
