/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.otp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.components.NBAlertDialog
import com.ayhancavdar.nexusbanking.core.ui.components.NBPrimaryButton
import com.ayhancavdar.nexusbanking.core.ui.components.NBTextField
import com.ayhancavdar.nexusbanking.core.ui.theme.NBColors
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.otp.presentation.state.OtpState
import java.util.Locale

@Composable
fun OtpScreen(
    onOtpSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OtpScreenContent(
        uiState = uiState,
        onOtpSuccess = onOtpSuccess,
        onNavigateBack = onNavigateBack,
        onOtpChange = viewModel::onOtpChanged,
        onAttemptLogin = viewModel::attemptOtpLogin,
        onDismissOtpError = viewModel::onDismissOtpError,
        onNavigationToAccounts = viewModel::onNavigationToAccounts,
    )
}

@Composable
private fun OtpScreenContent(
    uiState: OtpState,
    onOtpSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    onOtpChange: (String) -> Unit,
    onAttemptLogin: () -> Unit,
    onDismissOtpError: () -> Unit,
    onNavigationToAccounts: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            OtpAppBar(onNavigateBack = onNavigateBack)
        },
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
                Spacer(Modifier.height(dimensionResource(id = R.dimen.x8)))

                // Welcome Section with Logo
                OtpWelcomeSection()

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x40)))

                // OTP Card
                OtpCard(
                    uiState = uiState,
                    onOtpChange = onOtpChange,
                    onAttemptLogin = onAttemptLogin
                )

                Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))
            }
        }
    }

    uiState.otpApiError?.let { errorMessage ->
        NBAlertDialog(
            confirmButtonOnClick = onDismissOtpError,
            confirmButtonText = R.string.generic_okButton_title,
            dismissButtonOnClick = onDismissOtpError,
            dismissButtonText = R.string.generic_cancelButton_title,
            onDismissRequest = onDismissOtpError,
            text = errorMessage,
            title = R.string.generic_alert_title_error,
        )
    }

    LaunchedEffect(uiState.navigateToAccounts) {
        if (uiState.navigateToAccounts) {
            onOtpSuccess()
            onNavigationToAccounts()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OtpAppBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.x24)),
                    tint = NBColors.nearBlack
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = {},
    )
}

@Composable
private fun OtpWelcomeSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "NexusBanking Logo",
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.x128))
                .aspectRatio(1f)
                .shadow(
                    elevation = dimensionResource(id = R.dimen.x4),
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Color.White),
            contentScale = ContentScale.FillBounds
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x24)))

        Text(
            text = stringResource(id = R.string.otp_navigationBar_title),
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
private fun OtpCard(
    uiState: OtpState,
    onOtpChange: (String) -> Unit,
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
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x16))
            )

            // Phone number info
            Text(
                text = stringResource(R.string.otp_phoneNumberInfo_label_text, uiState.starredSmsNumber),
                style = MaterialTheme.typography.bodyMedium,
                color = NBColors.primaryGreenLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // Timer and progress section
            TimerProgressSection(
                formattedCountdown = uiState.formattedCountdown,
                sliderProgress = uiState.sliderProgress,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // OTP input field
            NBTextField(
                value = uiState.otpInput,
                onValueChange = { onOtpChange(it.filter(Char::isDigit).take(6)) },
                placeholderTextRes = R.string.login_password_textfield_placeholder,
                isError = uiState.otpError != null,
                errorTextRes = uiState.otpError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.x24))
            )

            // Continue button
            NBPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.x56)),
                onClick = onAttemptLogin,
                content = {
                    Text(
                        stringResource(id = R.string.otp_send_button_title).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.x12)),
                enabled = uiState.isContinueButtonEnabled,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerProgressSection(
    formattedCountdown: String,
    sliderProgress: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.otp_remainingTime_label_text),
                style = MaterialTheme.typography.bodyMedium,
                color = NBColors.nearBlack,
            )
            Text(
                text = formattedCountdown,
                style = MaterialTheme.typography.bodyMedium,
                color = NBColors.primaryGreen,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.x12)))

        Slider(
            onValueChange = { /* No op */ },
            thumb = {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.x16))
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    modifier = Modifier.height(dimensionResource(id = R.dimen.x4)),
                    sliderState = sliderState,
                    drawStopIndicator = null,
                    thumbTrackGapSize = dimensionResource(id = R.dimen.x0),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.outline,
                    )
                )
            },
            value = sliderProgress,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun OtpScreenPreview() {
    NexusBankingTheme {
        OtpScreenContent(
            uiState = OtpState(
                starredSmsNumber = "555 *** ** 12",
                otpInput = "1234",
                isContinueButtonEnabled = true,
                formattedCountdown = "00:45",
                sliderProgress = 0.75f
            ),
            onOtpSuccess = {},
            onNavigateBack = {},
            onOtpChange = {},
            onAttemptLogin = {},
            onDismissOtpError = {},
            onNavigationToAccounts = {},
        )
    }
}
