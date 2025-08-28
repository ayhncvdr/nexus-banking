/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.data.repository

import com.ayhancavdar.nexusbanking.core.network.AuthApiService
import com.ayhancavdar.nexusbanking.features.login.data.model.LoginResponse
import com.ayhancavdar.nexusbanking.features.login.data.model.SmsOtpNumber
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class LoginRepositoryImplTest {

    private val authApiService = mockk<AuthApiService>()

    private lateinit var repository: LoginRepositoryImpl

    @Before
    fun setup() {
        repository = LoginRepositoryImpl(
            authApiService = authApiService
        )
    }

    @Test
    fun `login - should return success with mapped user when API call succeeds`() = runTest {
        // Given
        val credentials = Credentials(username = "test123", password = "123456")
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        )
        val loginResponse = LoginResponse(
            customerName = "John Doe",
            smsOtpNumbers = smsOtpNumbers
        )

        coEvery { authApiService.login(any()) } returns Result.success(loginResponse)

        // When
        val result = repository.login(credentials)

        // Then
        assertTrue(result.isSuccess)
        val user = result.getOrNull()!!
        assertEquals("John Doe", user.customerName)
        assertEquals(smsOtpNumbers, user.smsOtpNumbers)

        coVerify { authApiService.login(any()) }
    }

    @Test
    fun `login - should return failure when API call fails`() = runTest {
        // Given
        val credentials = Credentials(username = "test123", password = "wrong")
        val exception = RuntimeException("Invalid credentials")

        coEvery { authApiService.login(any()) } returns Result.failure(exception)

        // When
        val result = repository.login(credentials)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify { authApiService.login(any()) }
    }

    @Test
    fun `login - should return failure when response has missing customer name`() = runTest {
        // Given
        val credentials = Credentials(username = "test123", password = "123456")
        val loginResponse = LoginResponse(
            customerName = null, // Missing customer name
            smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        )

        coEvery { authApiService.login(any()) } returns Result.success(loginResponse)

        // When
        val result = repository.login(credentials)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertEquals("Login successful but user data missing in response", exception.message)
    }

    @Test
    fun `login - should return failure when response has missing SMS OTP numbers`() = runTest {
        // Given
        val credentials = Credentials(username = "test123", password = "123456")
        val loginResponse = LoginResponse(
            customerName = "John Doe",
            smsOtpNumbers = null // Missing SMS OTP numbers
        )

        coEvery { authApiService.login(any()) } returns Result.success(loginResponse)

        // When
        val result = repository.login(credentials)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertEquals("Login successful but user data missing in response", exception.message)
    }

    @Test
    fun `login - should pass correct request to API service`() = runTest {
        // Given
        val credentials = Credentials(username = "testuser", password = "654321")
        val loginResponse = LoginResponse(
            customerName = "Jane Doe",
            smsOtpNumbers = listOf(SmsOtpNumber(number = "0987654321", starredNumber = "***4321"))
        )

        coEvery { authApiService.login(any()) } returns Result.success(loginResponse)

        // When
        repository.login(credentials)

        // Then
        coVerify {
            authApiService.login(
                match { request ->
                    request.username == "testuser" && request.password == "654321"
                }
            )
        }
    }
}
