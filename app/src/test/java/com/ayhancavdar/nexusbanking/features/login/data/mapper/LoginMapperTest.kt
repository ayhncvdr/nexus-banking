/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.data.mapper

import com.ayhancavdar.nexusbanking.features.login.data.model.LoginResponse
import com.ayhancavdar.nexusbanking.features.login.data.model.SmsOtpNumber
import com.ayhancavdar.nexusbanking.features.login.domain.model.Credentials
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginMapperTest {

    @Test
    fun `toRequest - should map credentials to login request correctly`() {
        // Given
        val credentials = Credentials(username = "testuser", password = "123456")

        // When
        val loginRequest = credentials.toRequest()

        // Then
        assertEquals("testuser", loginRequest.username)
        assertEquals("123456", loginRequest.password)
    }

    @Test
    fun `toRequest - should handle empty values`() {
        // Given
        val credentials = Credentials(username = "", password = "")

        // When
        val loginRequest = credentials.toRequest()

        // Then
        assertEquals("", loginRequest.username)
        assertEquals("", loginRequest.password)
    }

    @Test
    fun `toDomain - should map complete login response to user correctly`() {
        // Given
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1234567890", starredNumber = "***7890"),
            SmsOtpNumber(number = "0987654321", starredNumber = "***4321")
        )
        val loginResponse = LoginResponse(
            customerName = "John Doe",
            smsOtpNumbers = smsOtpNumbers
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertEquals("John Doe", user?.customerName)
        assertEquals(smsOtpNumbers, user?.smsOtpNumbers)
        assertEquals(2, user?.smsOtpNumbers?.size)
    }

    @Test
    fun `toDomain - should return null when customer name is null`() {
        // Given
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        )
        val loginResponse = LoginResponse(
            customerName = null,
            smsOtpNumbers = smsOtpNumbers
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertNull(user)
    }

    @Test
    fun `toDomain - should return null when sms otp numbers is null`() {
        // Given
        val loginResponse = LoginResponse(
            customerName = "John Doe",
            smsOtpNumbers = null
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertNull(user)
    }

    @Test
    fun `toDomain - should return null when both customer name and sms otp numbers are null`() {
        // Given
        val loginResponse = LoginResponse(
            customerName = null,
            smsOtpNumbers = null
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertNull(user)
    }

    @Test
    fun `toDomain - should handle empty sms otp numbers list`() {
        // Given
        val loginResponse = LoginResponse(
            customerName = "John Doe",
            smsOtpNumbers = emptyList()
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertEquals("John Doe", user?.customerName)
        assertEquals(emptyList(), user?.smsOtpNumbers)
    }

    @Test
    fun `toDomain - should handle single sms otp number`() {
        // Given
        val smsOtpNumber = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        val loginResponse = LoginResponse(
            customerName = "Jane Smith",
            smsOtpNumbers = listOf(smsOtpNumber)
        )

        // When
        val user = loginResponse.toDomain()

        // Then
        assertEquals("Jane Smith", user?.customerName)
        assertEquals(1, user?.smsOtpNumbers?.size)
        assertEquals(smsOtpNumber, user?.smsOtpNumbers?.first())
    }
}
