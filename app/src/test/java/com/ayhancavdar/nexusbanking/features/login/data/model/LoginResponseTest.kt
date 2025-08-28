/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.data.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LoginResponseTest {

    @Test
    fun `loginResponse creation - should create with valid data`() {
        // Given
        val customerName = "John Doe"
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        )

        // When
        val response = LoginResponse(customerName = customerName, smsOtpNumbers = smsOtpNumbers)

        // Then
        assertEquals(customerName, response.customerName)
        assertEquals(smsOtpNumbers, response.smsOtpNumbers)
    }

    @Test
    fun `loginResponse creation - should create with null values`() {
        // When
        val response = LoginResponse(customerName = null, smsOtpNumbers = null)

        // Then
        assertNull(response.customerName)
        assertNull(response.smsOtpNumbers)
    }

    @Test
    fun `loginResponse creation - should create with empty SMS list`() {
        // Given
        val customerName = "John Doe"

        // When
        val response = LoginResponse(customerName = customerName, smsOtpNumbers = emptyList())

        // Then
        assertEquals(customerName, response.customerName)
        assertTrue(response.smsOtpNumbers?.isEmpty() == true)
    }

    @Test
    fun `loginResponse equality - should be equal when all properties match`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val response1 = LoginResponse(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)
        val response2 = LoginResponse(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // Then
        assertEquals(response1, response2)
        assertEquals(response1.hashCode(), response2.hashCode())
    }

    @Test
    fun `loginResponse equality - should not be equal when customer name differs`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val response1 = LoginResponse(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)
        val response2 = LoginResponse(customerName = "Jane Smith", smsOtpNumbers = smsOtpNumbers)

        // Then
        assertNotEquals(response1, response2)
    }

    @Test
    fun `loginResponse copy - should create new instance with modified customer name`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val original = LoginResponse(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val copied = original.copy(customerName = "Jane Smith")

        // Then
        assertEquals("Jane Smith", copied.customerName)
        assertEquals(smsOtpNumbers, copied.smsOtpNumbers)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginResponse destructuring - should allow component access`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val response = LoginResponse(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val customerName = response.customerName
        val responseNumbers = response.smsOtpNumbers

        // Then
        assertEquals("John Doe", customerName)
        assertEquals(smsOtpNumbers, responseNumbers)
    }

    @Test
    fun `loginResponse toString - should contain class name and field names`() {
        // Given
        val response = LoginResponse(customerName = "John Doe", smsOtpNumbers = emptyList())

        // When
        val toString = response.toString()

        // Then
        assertTrue(toString.contains("LoginResponse"))
        assertTrue(toString.contains("customerName"))
        assertTrue(toString.contains("smsOtpNumbers"))
    }
}
