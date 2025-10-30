/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.domain.model

import com.ayhancavdar.nexusbanking.features.login.data.model.SmsOtpNumber
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun `user creation - should create with valid customer name and SMS OTP numbers`() {
        // Given
        val customerName = "John Doe"
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1234567890", starredNumber = "***7890"),
            SmsOtpNumber(number = "0987654321", starredNumber = "***4321")
        )

        // When
        val user = User(customerName = customerName, smsOtpNumbers = smsOtpNumbers)

        // Then
        assertEquals(customerName, user.customerName)
        assertEquals(smsOtpNumbers, user.smsOtpNumbers)
        assertEquals(2, user.smsOtpNumbers.size)
    }

    @Test
    fun `user creation - should create with single SMS OTP number`() {
        // Given
        val customerName = "Jane Smith"
        val smsOtpNumber = SmsOtpNumber(number = "5555555555", starredNumber = "***5555")

        // When
        val user = User(customerName = customerName, smsOtpNumbers = listOf(smsOtpNumber))

        // Then
        assertEquals(customerName, user.customerName)
        assertEquals(1, user.smsOtpNumbers.size)
        assertEquals(smsOtpNumber, user.smsOtpNumbers.first())
    }

    @Test
    fun `user creation - should create with empty SMS OTP numbers list`() {
        // Given
        val customerName = "Empty Numbers User"

        // When
        val user = User(customerName = customerName, smsOtpNumbers = emptyList())

        // Then
        assertEquals(customerName, user.customerName)
        assertTrue(user.smsOtpNumbers.isEmpty())
    }

    @Test
    fun `user equality - should be equal when all properties match`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val user1 = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)
        val user2 = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // Then
        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `user equality - should not be equal when customer name differs`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val user1 = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)
        val user2 = User(customerName = "Jane Smith", smsOtpNumbers = smsOtpNumbers)

        // Then
        assertNotEquals(user1, user2)
    }

    @Test
    fun `user equality - should not be equal when SMS OTP numbers differ`() {
        // Given
        val smsOtpNumbers1 = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val smsOtpNumbers2 = listOf(SmsOtpNumber(number = "0987654321", starredNumber = "***4321"))
        val user1 = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers1)
        val user2 = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers2)

        // Then
        assertNotEquals(user1, user2)
    }

    @Test
    fun `user copy - should create new instance with modified customer name`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val original = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val copied = original.copy(customerName = "Jane Smith")

        // Then
        assertEquals("Jane Smith", copied.customerName)
        assertEquals(smsOtpNumbers, copied.smsOtpNumbers)
        assertNotEquals(original, copied)
    }

    @Test
    fun `user copy - should create new instance with modified SMS OTP numbers`() {
        // Given
        val originalSmsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val newSmsOtpNumbers = listOf(SmsOtpNumber(number = "0987654321", starredNumber = "***4321"))
        val original = User(customerName = "John Doe", smsOtpNumbers = originalSmsOtpNumbers)

        // When
        val copied = original.copy(smsOtpNumbers = newSmsOtpNumbers)

        // Then
        assertEquals("John Doe", copied.customerName)
        assertEquals(newSmsOtpNumbers, copied.smsOtpNumbers)
        assertNotEquals(original, copied)
    }

    @Test
    fun `user copy - should create identical instance when no parameters changed`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val original = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val copied = original.copy()

        // Then
        assertEquals(original, copied)
        assertEquals(original.customerName, copied.customerName)
        assertEquals(original.smsOtpNumbers, copied.smsOtpNumbers)
    }

    @Test
    fun `user destructuring - should allow component access`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val user = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val (customerName, smsNumbers) = user

        // Then
        assertEquals("John Doe", customerName)
        assertEquals(smsOtpNumbers, smsNumbers)
    }

    @Test
    fun `user toString - should contain class name and field names`() {
        // Given
        val smsOtpNumbers = listOf(SmsOtpNumber(number = "1234567890", starredNumber = "***7890"))
        val user = User(customerName = "John Doe", smsOtpNumbers = smsOtpNumbers)

        // When
        val toString = user.toString()

        // Then
        assertTrue(toString.contains("User"))
        assertTrue(toString.contains("customerName"))
        assertTrue(toString.contains("smsOtpNumbers"))
    }

    @Test
    fun `user with multiple SMS OTP numbers - should maintain order`() {
        // Given
        val smsOtpNumbers = listOf(
            SmsOtpNumber(number = "1111111111", starredNumber = "***1111"),
            SmsOtpNumber(number = "2222222222", starredNumber = "***2222"),
            SmsOtpNumber(number = "3333333333", starredNumber = "***3333")
        )

        // When
        val user = User(customerName = "Multi Number User", smsOtpNumbers = smsOtpNumbers)

        // Then
        assertEquals(3, user.smsOtpNumbers.size)
        assertEquals("***1111", user.smsOtpNumbers[0].starredNumber)
        assertEquals("***2222", user.smsOtpNumbers[1].starredNumber)
        assertEquals("***3333", user.smsOtpNumbers[2].starredNumber)
    }
}
