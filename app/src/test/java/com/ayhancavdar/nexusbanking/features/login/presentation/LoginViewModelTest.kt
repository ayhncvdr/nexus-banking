/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.presentation

import androidx.lifecycle.SavedStateHandle
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.common.NBSharedPrefs
import com.ayhancavdar.nexusbanking.features.login.domain.model.User
import com.ayhancavdar.nexusbanking.features.login.domain.repository.LoginRepository
import com.ayhancavdar.nexusbanking.features.login.presentation.state.LoginState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val loginRepository = mockk<LoginRepository>()
    private val sharedPreferences = mockk<NBSharedPrefs>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Create a MutableStateFlow that will be shared between getStateFlow and set operations
        val stateFlow = MutableStateFlow(LoginState())

        // Mock getStateFlow to return our shared flow
        every { savedStateHandle.getStateFlow(any<String>(), any<LoginState>()) } returns stateFlow

        // Mock the setter to update the same flow
        every { savedStateHandle[any<String>()] = any<LoginState>() } answers {
            val newState = secondArg<LoginState>()
            stateFlow.value = newState
        }

        every { sharedPreferences.getString(any(), any()) } returns ""

        viewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            sharedPreferences = sharedPreferences,
            ioDispatcher = testDispatcher
        )
    }

    // Username Validation Tests
    @Test
    fun `username validation - valid input should clear error`() = runTest {
        viewModel.onUsernameChanged("test123")

        val state = viewModel.uiState.first()
        assertEquals("test123", state.usernameInput)
        assertNull(state.usernameError)
    }

    @Test
    fun `username validation - short input should show length error`() = runTest {
        viewModel.onUsernameChanged("abc")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)
    }

    @Test
    fun `username validation - long input should show length error`() = runTest {
        viewModel.onUsernameChanged("verylongusername")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)
    }

    @Test
    fun `username validation - blank input should show required error`() = runTest {
        viewModel.onUsernameChanged("")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_required, state.usernameError)
    }

    @Test
    fun `username validation - special characters should show alphanumeric error`() = runTest {
        viewModel.onUsernameChanged("test@123")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_alphanumeric, state.usernameError)
    }

    // Password Validation Tests
    @Test
    fun `password validation - valid input should clear error`() = runTest {
        viewModel.onPasswordChanged("123456")

        val state = viewModel.uiState.first()
        assertEquals("123456", state.passwordInput)
        assertNull(state.passwordError)
    }

    @Test
    fun `password validation - short input should show length error`() = runTest {
        viewModel.onPasswordChanged("123")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_length, state.passwordError)
    }

    @Test
    fun `password validation - long input should show length error`() = runTest {
        viewModel.onPasswordChanged("1234567")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_length, state.passwordError)
    }

    @Test
    fun `password validation - blank input should show required error`() = runTest {
        viewModel.onPasswordChanged("")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_required, state.passwordError)
    }

    @Test
    fun `password validation - non-numeric input should show numeric error`() = runTest {
        viewModel.onPasswordChanged("12345a")

        val state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_numeric, state.passwordError)
    }

    // Login Button State Tests
    @Test
    fun `login button - should be enabled when both fields are valid`() = runTest {
        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123456")

        val state = viewModel.uiState.first()
        assertTrue(state.isLoginButtonEnabled)
    }

    @Test
    fun `login button - should be disabled when username is invalid`() = runTest {
        viewModel.onUsernameChanged("abc") // too short
        viewModel.onPasswordChanged("123456")

        val state = viewModel.uiState.first()
        assertFalse(state.isLoginButtonEnabled)
    }

    @Test
    fun `login button - should be disabled when password is invalid`() = runTest {
        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123") // too short

        val state = viewModel.uiState.first()
        assertFalse(state.isLoginButtonEnabled)
    }

    // Remember Me Tests
    @Test
    fun `remember me - should update state when checked`() = runTest {
        viewModel.onRememberMeChanged(true)

        val state = viewModel.uiState.first()
        assertTrue(state.rememberMe)
    }

    @Test
    fun `remember me - should remove saved username when unchecked`() = runTest {
        viewModel.onRememberMeChanged(false)

        verify { sharedPreferences.remove("username") }
    }

    // Login Process Tests
    @Test
    fun `attemptLogin - should succeed with valid credentials`() = runTest {
        // Given
        val mockUser = mockk<User>(relaxed = true)
        coEvery { loginRepository.login(any()) } returns Result.success(mockUser)

        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123456")

        // When
        viewModel.attemptLogin()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertNull(state.loginApiError)
    }

    @Test
    fun `attemptLogin - should handle network error`() = runTest {
        // Given
        val networkException = NBNetworkException(errorMessage = "Network error")
        coEvery { loginRepository.login(any()) } returns Result.failure(networkException)

        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123456")

        // When
        viewModel.attemptLogin()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals("Network error", state.loginApiError)
    }

    @Test
    fun `attemptLogin - should not proceed when form is invalid`() = runTest {
        // Given
        viewModel.onUsernameChanged("abc") // invalid
        viewModel.onPasswordChanged("123456")

        // When
        viewModel.attemptLogin()

        // Then
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)
    }

    @Test
    fun `attemptLogin - should save username when remember me is checked`() = runTest {
        // Given
        val mockUser = mockk<User>(relaxed = true)
        coEvery { loginRepository.login(any()) } returns Result.success(mockUser)

        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123456")
        viewModel.onRememberMeChanged(true)

        // When
        viewModel.attemptLogin()
        advanceUntilIdle()

        // Then
        verify { sharedPreferences.setString("username", "test123") }
    }

    // Error Handling Tests
    @Test
    fun `onDismissLoginError - should clear login error`() = runTest {
        // Given - set an error first
        val networkException = NBNetworkException(errorMessage = "Network error")
        coEvery { loginRepository.login(any()) } returns Result.failure(networkException)
        viewModel.onUsernameChanged("test123")
        viewModel.onPasswordChanged("123456")
        viewModel.attemptLogin()
        advanceUntilIdle()

        // When
        viewModel.onDismissLoginError()

        // Then
        val state = viewModel.uiState.first()
        assertNull(state.loginApiError)
    }

    // Input Changes Clear Errors Tests
    @Test
    fun `onUsernameChanged - should clear login API error`() = runTest {
        viewModel.onUsernameChanged("test123")

        val state = viewModel.uiState.first()
        assertNull(state.loginApiError)
    }

    @Test
    fun `onPasswordChanged - should clear login API error`() = runTest {
        viewModel.onPasswordChanged("123456")

        val state = viewModel.uiState.first()
        assertNull(state.loginApiError)
    }

    // Navigation Tests
    @Test
    fun `onNavigationToOtp - should clear starred SMS number`() = runTest {
        viewModel.onNavigationToOtp()

        val state = viewModel.uiState.first()
        assertNull(state.starredSmsNumber)
    }
}
