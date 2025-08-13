/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.presentation.state

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginStateTest {

    @Test
    fun `loginState creation - should create with default values`() {
        // When
        val state = LoginState()

        // Then
        assertEquals("", state.usernameInput)
        assertEquals("", state.passwordInput)
        assertFalse(state.rememberMe)
        assertNull(state.usernameError)
        assertNull(state.passwordError)
        assertFalse(state.isLoginButtonEnabled)
        assertFalse(state.isLoading)
        assertNull(state.loginApiError)
        assertNull(state.starredSmsNumber)
    }

    @Test
    fun `loginState creation - should create with custom values`() {
        // Given
        val state = LoginState(
            usernameInput = "testuser",
            passwordInput = "123456",
            rememberMe = true,
            usernameError = 12345,
            passwordError = 67890,
            isLoginButtonEnabled = true,
            isLoading = true,
            loginApiError = "Network error",
            starredSmsNumber = "***1234"
        )

        // Then
        assertEquals("testuser", state.usernameInput)
        assertEquals("123456", state.passwordInput)
        assertTrue(state.rememberMe)
        assertEquals(12345, state.usernameError)
        assertEquals(67890, state.passwordError)
        assertTrue(state.isLoginButtonEnabled)
        assertTrue(state.isLoading)
        assertEquals("Network error", state.loginApiError)
        assertEquals("***1234", state.starredSmsNumber)
    }

    @Test
    fun `loginState equality - should be equal when all properties match`() {
        // Given
        val state1 = LoginState(
            usernameInput = "test",
            passwordInput = "123456",
            rememberMe = true,
            isLoginButtonEnabled = true
        )
        val state2 = LoginState(
            usernameInput = "test",
            passwordInput = "123456",
            rememberMe = true,
            isLoginButtonEnabled = true
        )

        // Then
        assertEquals(state1, state2)
        assertEquals(state1.hashCode(), state2.hashCode())
    }

    @Test
    fun `loginState equality - should not be equal when username differs`() {
        // Given
        val state1 = LoginState(usernameInput = "test1")
        val state2 = LoginState(usernameInput = "test2")

        // Then
        assertNotEquals(state1, state2)
    }

    @Test
    fun `loginState equality - should not be equal when loading state differs`() {
        // Given
        val state1 = LoginState(isLoading = true)
        val state2 = LoginState(isLoading = false)

        // Then
        assertNotEquals(state1, state2)
    }

    @Test
    fun `loginState copy - should create new instance with modified username`() {
        // Given
        val original = LoginState(usernameInput = "original", passwordInput = "123456")

        // When
        val copied = original.copy(usernameInput = "modified")

        // Then
        assertEquals("modified", copied.usernameInput)
        assertEquals("123456", copied.passwordInput)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginState copy - should create new instance with modified loading state`() {
        // Given
        val original = LoginState(isLoading = false, usernameInput = "test")

        // When
        val copied = original.copy(isLoading = true)

        // Then
        assertTrue(copied.isLoading)
        assertEquals("test", copied.usernameInput)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginState copy - should create identical instance when no parameters changed`() {
        // Given
        val original = LoginState(
            usernameInput = "test",
            passwordInput = "123456",
            rememberMe = true
        )

        // When
        val copied = original.copy()

        // Then
        assertEquals(original, copied)
    }

    @Test
    fun `loginState copy - should handle error states correctly`() {
        // Given
        val original = LoginState()

        // When
        val copied = original.copy(
            usernameError = 12345,
            passwordError = 67890,
            loginApiError = "API Error"
        )

        // Then
        assertEquals(12345, copied.usernameError)
        assertEquals(67890, copied.passwordError)
        assertEquals("API Error", copied.loginApiError)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginState copy - should handle button enabled state correctly`() {
        // Given
        val original = LoginState(isLoginButtonEnabled = false)

        // When
        val copied = original.copy(isLoginButtonEnabled = true)

        // Then
        assertTrue(copied.isLoginButtonEnabled)
        assertFalse(original.isLoginButtonEnabled)
    }

    @Test
    fun `loginState copy - should handle starred SMS number correctly`() {
        // Given
        val original = LoginState()

        // When
        val copied = original.copy(starredSmsNumber = "***5678")

        // Then
        assertEquals("***5678", copied.starredSmsNumber)
        assertNull(original.starredSmsNumber)
    }

    @Test
    fun `loginState destructuring - should allow component access`() {
        // Given
        val state = LoginState(
            usernameInput = "testuser",
            passwordInput = "123456",
            rememberMe = true,
            usernameError = null,
            passwordError = null,
            isLoginButtonEnabled = true,
            isLoading = false,
            loginApiError = null,
            starredSmsNumber = "***1234"
        )

        // When
        val (
            username, password, rememberMe, usernameError, passwordError,
            isButtonEnabled, isLoading, apiError, starredSms
        ) = state

        // Then
        assertEquals("testuser", username)
        assertEquals("123456", password)
        assertTrue(rememberMe)
        assertNull(usernameError)
        assertNull(passwordError)
        assertTrue(isButtonEnabled)
        assertFalse(isLoading)
        assertNull(apiError)
        assertEquals("***1234", starredSms)
    }

    @Test
    fun `loginState toString - should contain class name and field names`() {
        // Given
        val state = LoginState(usernameInput = "test", isLoading = true)

        // When
        val toString = state.toString()

        // Then
        assertTrue(toString.contains("LoginState"))
        assertTrue(toString.contains("usernameInput"))
        assertTrue(toString.contains("isLoading"))
    }

    @Test
    fun `loginState with all error states - should handle multiple errors`() {
        // Given
        val state = LoginState(
            usernameError = 1001,
            passwordError = 1002,
            loginApiError = "Network connection failed",
            isLoginButtonEnabled = false,
            isLoading = false
        )

        // Then
        assertEquals(1001, state.usernameError)
        assertEquals(1002, state.passwordError)
        assertEquals("Network connection failed", state.loginApiError)
        assertFalse(state.isLoginButtonEnabled)
        assertFalse(state.isLoading)
    }

    @Test
    fun `loginState in loading state - should have correct configuration`() {
        // Given
        val state = LoginState(
            usernameInput = "test123",
            passwordInput = "123456",
            isLoading = true,
            isLoginButtonEnabled = false,
            loginApiError = null
        )

        // Then
        assertTrue(state.isLoading)
        assertFalse(state.isLoginButtonEnabled)
        assertNull(state.loginApiError)
        assertEquals("test123", state.usernameInput)
        assertEquals("123456", state.passwordInput)
    }
}
