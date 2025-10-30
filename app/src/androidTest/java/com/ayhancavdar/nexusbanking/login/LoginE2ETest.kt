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
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ayhancavdar.nexusbanking.MainActivity
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.common.NBSharedPrefs
import com.ayhancavdar.nexusbanking.core.navigation.AppNavHost
import com.ayhancavdar.nexusbanking.core.navigation.NexusBankingRoute
import com.ayhancavdar.nexusbanking.core.ui.components.AppBackground
import com.ayhancavdar.nexusbanking.core.ui.theme.NexusBankingTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginE2ETest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var sharedPrefs: NBSharedPrefs

    private val context: Context
        get() = composeTestRule.activity.applicationContext

    @Before
    fun setUp() {
        hiltRule.inject()
        clearLoginPreferences()

        composeTestRule.activity.setContent {
            NexusBankingTheme {
                val navController = rememberNavController()
                AppBackground {
                    AppNavHost(
                        modifier = Modifier,
                        navController = navController,
                        startDestination = NexusBankingRoute.Login
                    )
                }
            }
        }
    }

    @Test
    fun testLoginScreenDisplaysCorrectly() {
        // Verify welcome section
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_welcome_title))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_welcome_subtitle))
            .assertIsDisplayed()

        // Verify login form
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_form_title))
            .assertIsDisplayed()

        // Verify text fields
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_username_textfield_placeholder))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_password_textfield_placeholder))
            .assertIsDisplayed()

        // Verify remember me switch
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_rememberMe_label_text))
            .assertIsDisplayed()

        // Verify login button
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun testUsernameValidationErrors() {
        // Find text fields by their text input capability
        val textFields = composeTestRule.onAllNodes(hasSetTextAction())

        // Test username too short
        textFields[0] // First text field (username)
            .performTextInput("test")

        textFields[1] // Second text field (password)
            .performTextInput("123456")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_validation_error_username_length))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()

        // Clear and test valid username
        textFields[0]
            .performTextClearance()

        textFields[0]
            .performTextInput("testuser")

        // Error should disappear and button should be enabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_validation_error_username_length))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsEnabled()
    }

    @Test
    fun testPasswordValidationErrors() {
        val textFields = composeTestRule.onAllNodes(hasSetTextAction())

        // Enter valid username
        textFields[0]
            .performTextInput("testuser")

        // Test password too short
        textFields[1]
            .performTextInput("12345")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_validation_error_password_length))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()

        // Test non-numeric password
        textFields[1]
            .performTextClearance()

        textFields[1]
            .performTextInput("abcdef")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_validation_error_password_numeric))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()

        // Enter valid password
        textFields[1]
            .performTextClearance()

        textFields[1]
            .performTextInput("123456")

        // Error should disappear and button should be enabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_validation_error_password_numeric))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsEnabled()
    }

    @Test
    fun testRememberMeToggle() {
        val rememberMeText = context.getString(R.string.login_rememberMe_label_text)

        // Initially should be unchecked (assuming default state)
        composeTestRule
            .onNodeWithText(rememberMeText)
            .assertIsDisplayed()

        // Click to toggle
        composeTestRule
            .onNodeWithText(rememberMeText)
            .performClick()

        // Enter valid credentials
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_username_textfield_placeholder))
            .performTextInput("testuser")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_password_textfield_placeholder))
            .performTextInput("123456")

        // Login button should be enabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsEnabled()
    }

    @Test
    fun testCompleteLoginFlow() {
        // Enter valid credentials
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_username_textfield_placeholder))
            .performTextInput("testuser")

        composeTestRule
            .onNodeWithText(context.getString(R.string.login_password_textfield_placeholder))
            .performTextInput("123456")

        // Verify button is enabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsEnabled()

        // Toggle remember me
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_rememberMe_label_text))
            .performClick()

        // Click login button
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .performClick()

        // Note: For complete E2E testing, you'd need to mock the login repository
        // to test actual login success/failure scenarios
    }

    @Test
    fun testEmptyFieldsDisableLoginButton() {
        val textFields = composeTestRule.onAllNodes(hasSetTextAction())

        // Button should start disabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()

        // Enter only username
        textFields[0] // First text field (username)
            .performTextInput("testuser")

        // Button should still be disabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()

        // Enter password
        textFields[1] // Second text field (password)
            .performTextInput("123456")

        // Now button should be enabled
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsEnabled()

        // Clear username
        textFields[0]
            .performTextClearance()

        // Button should be disabled again
        composeTestRule
            .onNodeWithText(context.getString(R.string.login_login_button_title).uppercase())
            .assertIsNotEnabled()
    }

    private fun clearLoginPreferences() {
        sharedPrefs.remove("username")
    }
}