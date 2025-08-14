/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.integration

import androidx.lifecycle.SavedStateHandle
import com.ayhancavdar.nexusbanking.R
import com.ayhancavdar.nexusbanking.core.common.NBNetworkException
import com.ayhancavdar.nexusbanking.core.common.NBSharedPrefs
import com.ayhancavdar.nexusbanking.features.login.data.model.SmsOtpNumber
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import com.ayhancavdar.nexusbanking.features.login.domain.model.User
import com.ayhancavdar.nexusbanking.features.login.domain.repository.LoginRepository
import com.ayhancavdar.nexusbanking.features.login.presentation.LoginViewModel
import com.ayhancavdar.nexusbanking.features.login.presentation.state.LoginState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
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
class LoginIntegrationTest {

    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val loginRepository = mockk<LoginRepository>()
    private val sharedPreferences = mockk<NBSharedPrefs>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel
    private val mockStateFlow = MutableStateFlow(LoginState())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { savedStateHandle.getStateFlow(any<String>(), any<LoginState>()) } returns mockStateFlow
        every { savedStateHandle.get<LoginState>(any()) } returns LoginState()
        every { savedStateHandle[any<String>()] = any<LoginState>() } answers {
            mockStateFlow.value = secondArg()
        }
        every { sharedPreferences.getString(any(), any()) } returns ""

        viewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            sharedPreferences = sharedPreferences,
            ioDispatcher = testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `complete login flow with valid credentials should succeed and save username when remember me is checked`() =
        runTest {
            // Given
            val validUsername = "testuser"
            val validPassword = "123456"
            val mockUser = User(
                smsOtpNumbers = listOf(
                    SmsOtpNumber(starredNumber = "***123")
                ),
                customerName = "Ayhan Cavdar"
            )

            coEvery { loginRepository.login(any()) } returns Result.success(mockUser)

            // When - Enter valid credentials
            viewModel.onUsernameChanged(validUsername)
            viewModel.onPasswordChanged(validPassword)
            viewModel.onRememberMeChanged(true)

            advanceUntilIdle()

            // Then - Login button should be enabled
            var state = viewModel.uiState.first()
            assertTrue(state.isLoginButtonEnabled)
            assertNull(state.usernameError)
            assertNull(state.passwordError)

            // When - Attempt login
            viewModel.attemptLogin()
            advanceUntilIdle()

            // Then - Login should succeed
            state = viewModel.uiState.first()
            assertFalse(state.isLoading)
            assertNull(state.loginApiError)
            assertEquals("***123", state.starredSmsNumber)

            // Verify repository was called with correct credentials
            coVerify {
                loginRepository.login(Credentials(validUsername, validPassword))
            }

            // Verify username was saved due to remember me
            verify {
                sharedPreferences.setString("username", validUsername)
            }
        }

    @Test
    fun `login flow with invalid credentials should show validation errors`() = runTest {
        // When - Enter invalid credentials
        viewModel.onUsernameChanged("abc") // Too short
        viewModel.onPasswordChanged("123") // Too short

        advanceUntilIdle()

        // Then - Should show validation errors and disable login button
        val state = viewModel.uiState.first()
        assertFalse(state.isLoginButtonEnabled)
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)
        assertEquals(R.string.login_validation_error_password_length, state.passwordError)
    }

    @Test
    fun `login flow with network error should show error message`() = runTest {
        // Given
        val validUsername = "testuser"
        val validPassword = "123456"
        val errorMessage = "Network connection failed"
        val networkException = NBNetworkException(errorMessage = errorMessage)

        coEvery { loginRepository.login(any()) } returns Result.failure(networkException)

        // When - Enter valid credentials and attempt login
        viewModel.onUsernameChanged(validUsername)
        viewModel.onPasswordChanged(validPassword)
        advanceUntilIdle()

        viewModel.attemptLogin()
        advanceUntilIdle()

        // Then - Should show error message
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.loginApiError)
        assertTrue(state.isLoginButtonEnabled) // Should re-enable button after error
        assertNull(state.starredSmsNumber)
    }

    @Test
    fun `login flow without remember me should not save username`() = runTest {
        // Given
        val validUsername = "testuser"
        val validPassword = "123456"
        val mockUser = User(
            smsOtpNumbers = listOf(SmsOtpNumber(starredNumber = "***123")),
            customerName = "Ayhan Cavdar"
        )

        coEvery { loginRepository.login(any()) } returns Result.success(mockUser)

        // When - Login without remember me checked
        viewModel.onUsernameChanged(validUsername)
        viewModel.onPasswordChanged(validPassword)
        viewModel.onRememberMeChanged(false)

        advanceUntilIdle()

        viewModel.attemptLogin()
        advanceUntilIdle()

        // Then - Username should not be saved
        verify {
            sharedPreferences.remove("username")
        }
        verify(exactly = 0) {
            sharedPreferences.setString("username", any())
        }
    }

    @Test
    fun `username validation should handle various input scenarios`() = runTest {
        // Test empty username
        viewModel.onUsernameChanged("")
        advanceUntilIdle()
        var state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_required, state.usernameError)

        // Test too short username
        viewModel.onUsernameChanged("abc")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)

        // Test too long username
        viewModel.onUsernameChanged("verylongusername")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_username_length, state.usernameError)

        // Test valid username
        viewModel.onUsernameChanged("user123")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertNull(state.usernameError)
    }

    @Test
    fun `password validation should handle various input scenarios`() = runTest {
        // Test empty password
        viewModel.onPasswordChanged("")
        advanceUntilIdle()
        var state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_required, state.passwordError)

        // Test too short password
        viewModel.onPasswordChanged("123")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_length, state.passwordError)

        // Test non-numeric password
        viewModel.onPasswordChanged("abcdef")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertEquals(R.string.login_validation_error_password_numeric, state.passwordError)

        // Test valid password
        viewModel.onPasswordChanged("123456")
        advanceUntilIdle()
        state = viewModel.uiState.first()
        assertNull(state.passwordError)
    }

    @Test
    fun `dismiss login error should clear error state`() = runTest {
        // Given - Login with error
        val errorMessage = "Login failed"
        coEvery { loginRepository.login(any()) } returns Result.failure(Exception(errorMessage))

        viewModel.onUsernameChanged("testuser")
        viewModel.onPasswordChanged("123456")
        advanceUntilIdle()

        viewModel.attemptLogin()
        advanceUntilIdle()

        var state = viewModel.uiState.first()
        assertNotNull(state.loginApiError)

        // When - Dismiss error
        viewModel.onDismissLoginError()
        advanceUntilIdle()

        // Then - Error should be cleared
        state = viewModel.uiState.first()
        assertNull(state.loginApiError)
    }

    @Test
    fun `navigation to OTP should clear starred SMS number`() = runTest {
        // Given - Successful login with SMS number
        val mockUser = User(
            smsOtpNumbers = listOf(SmsOtpNumber(starredNumber = "***123")),
            customerName = "Ayhan Cavdar"
        )
        coEvery { loginRepository.login(any()) } returns Result.success(mockUser)

        viewModel.onUsernameChanged("testuser")
        viewModel.onPasswordChanged("123456")
        advanceUntilIdle()

        viewModel.attemptLogin()
        advanceUntilIdle()

        var state = viewModel.uiState.first()
        assertEquals("***123", state.starredSmsNumber)

        // When - Navigate to OTP
        viewModel.onNavigationToOtp()
        advanceUntilIdle()

        // Then - SMS number should be cleared
        state = viewModel.uiState.first()
        assertNull(state.starredSmsNumber)
    }

    @Test
    fun `load saved username on initialization should populate username field`() = runTest {
        // Given - Saved username exists
        val savedUsername = "saveduser"
        every { sharedPreferences.getString("username", "") } returns savedUsername

        // When - Create new ViewModel (simulating app restart)
        val newViewModel = LoginViewModel(
            savedStateHandle = savedStateHandle,
            loginRepository = loginRepository,
            sharedPreferences = sharedPreferences,
            ioDispatcher = testDispatcher
        )
        advanceUntilIdle()

        // Then - Username should be loaded
        val state = newViewModel.uiState.first()
        assertEquals(savedUsername, state.usernameInput)
    }
}
