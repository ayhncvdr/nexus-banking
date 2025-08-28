/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import com.ayhancavdar.nexusbanking.features.login.presentation.LoginScreenContent
import com.ayhancavdar.nexusbanking.features.login.presentation.state.LoginState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysAllRequiredElements() {
        composeTestRule.setContent {
            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(),
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

        // Use ApplicationProvider to get context and access string resources
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Verify main elements are displayed using string resources
        composeTestRule.onNodeWithText(
            context.getString(R.string.login_welcome_title)
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_welcome_subtitle)
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_form_title)
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_rememberMe_label_text)
        ).assertIsDisplayed()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_login_button_title).uppercase()
        ).assertIsDisplayed()
    }

    @Test
    fun loginButton_isDisabledWhenFormIsEmpty() {
        composeTestRule.setContent {
            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(isLoginButtonEnabled = false),
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

        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_login_button_title).uppercase()
        ).assertIsNotEnabled()
    }

    @Test
    fun loginButton_isEnabledWhenFormIsValid() {
        composeTestRule.setContent {
            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(
                        usernameInput = "testuser",
                        passwordInput = "123456",
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

        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_login_button_title).uppercase()
        ).assertIsEnabled()
    }

    @Test
    fun usernameField_acceptsTextInput() {
        var capturedUsername = ""

        composeTestRule.setContent {
            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(),
                    onLoginSuccess = {},
                    onUsernameChange = { capturedUsername = it },
                    onPasswordChange = {},
                    onRememberMeChange = {},
                    onAttemptLogin = {},
                    onDismissLoginError = {},
                    onNavigationToOtp = {}
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()

        // Find the text field with placeholder and enter text
        composeTestRule.onNodeWithText(
            context.getString(R.string.login_username_textfield_placeholder)
        ).performTextInput("testuser")

        // Verify the callback was called
        assert(capturedUsername == "testuser")
    }

    @Test
    fun loginButton_triggersLoginAttempt() {
        var loginAttempted = false

        composeTestRule.setContent {
            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(
                        usernameInput = "testuser",
                        passwordInput = "123456",
                        isLoginButtonEnabled = true
                    ),
                    onLoginSuccess = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onRememberMeChange = {},
                    onAttemptLogin = { loginAttempted = true },
                    onDismissLoginError = {},
                    onNavigationToOtp = {}
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()

        composeTestRule.onNodeWithText(
            context.getString(R.string.login_login_button_title).uppercase()
        ).performClick()

        assert(loginAttempted)
    }

    @Test
    fun rememberMeSwitch_canBeToggled() {
        var callbackValue = false

        composeTestRule.setContent {
            var rememberMeState by remember { mutableStateOf(false) }

            NexusBankingTheme {
                LoginScreenContent(
                    uiState = LoginState(rememberMe = rememberMeState),
                    onLoginSuccess = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onRememberMeChange = {
                        callbackValue = it
                        rememberMeState = it
                    },
                    onAttemptLogin = {},
                    onDismissLoginError = {},
                    onNavigationToOtp = {}
                )
            }
        }

        val context = ApplicationProvider.getApplicationContext<Context>()

        // Click the "Remember Me" text
        composeTestRule.onNodeWithText(
            context.getString(R.string.login_rememberMe_label_text)
        ).performClick()

        // Verify the callback was called with true
        assert(callbackValue)
    }
}
