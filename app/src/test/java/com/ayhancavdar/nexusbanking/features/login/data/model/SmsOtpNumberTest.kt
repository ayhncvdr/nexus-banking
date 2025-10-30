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
import kotlin.test.assertTrue

class SmsOtpNumberTest {

    @Test
    fun `smsOtpNumber creation - should create with valid values`() {
        // Given
        val number = "1234567890"
        val starredNumber = "***7890"

        // When
        val smsOtpNumber = SmsOtpNumber(number = number, starredNumber = starredNumber)

        // Then
        assertEquals(number, smsOtpNumber.number)
        assertEquals(starredNumber, smsOtpNumber.starredNumber)
    }

    @Test
    fun `smsOtpNumber creation - should create with empty values`() {
        // Given
        val number = ""
        val starredNumber = ""

        // When
        val smsOtpNumber = SmsOtpNumber(number = number, starredNumber = starredNumber)

        // Then
        assertEquals("", smsOtpNumber.number)
        assertEquals("", smsOtpNumber.starredNumber)
    }

    @Test
    fun `smsOtpNumber equality - should be equal when all properties match`() {
        // Given
        val smsOtpNumber1 = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        val smsOtpNumber2 = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")

        // Then
        assertEquals(smsOtpNumber1, smsOtpNumber2)
        assertEquals(smsOtpNumber1.hashCode(), smsOtpNumber2.hashCode())
    }

    @Test
    fun `smsOtpNumber equality - should not be equal when number differs`() {
        // Given
        val smsOtpNumber1 = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        val smsOtpNumber2 = SmsOtpNumber(number = "0987654321", starredNumber = "***7890")

        // Then
        assertNotEquals(smsOtpNumber1, smsOtpNumber2)
    }

    @Test
    fun `smsOtpNumber equality - should not be equal when starred number differs`() {
        // Given
        val smsOtpNumber1 = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")
        val smsOtpNumber2 = SmsOtpNumber(number = "1234567890", starredNumber = "***4321")

        // Then
        assertNotEquals(smsOtpNumber1, smsOtpNumber2)
    }

    @Test
    fun `smsOtpNumber copy - should create new instance with modified number`() {
        // Given
        val original = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")

        // When
        val copied = original.copy(number = "0987654321")

        // Then
        assertEquals("0987654321", copied.number)
        assertEquals("***7890", copied.starredNumber)
        assertNotEquals(original, copied)
    }

    @Test
    fun `smsOtpNumber copy - should create new instance with modified starred number`() {
        // Given
        val original = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")

        // When
        val copied = original.copy(starredNumber = "***4321")

        // Then
        assertEquals("1234567890", copied.number)
        assertEquals("***4321", copied.starredNumber)
        assertNotEquals(original, copied)
    }

    @Test
    fun `smsOtpNumber destructuring - should allow component access`() {
        // Given
        val smsOtpNumber = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")

        // When
        val (number, starredNumber) = smsOtpNumber

        // Then
        assertEquals("1234567890", number)
        assertEquals("***7890", starredNumber)
    }

    @Test
    fun `smsOtpNumber toString - should contain class name and field names`() {
        // Given
        val smsOtpNumber = SmsOtpNumber(number = "1234567890", starredNumber = "***7890")

        // When
        val toString = smsOtpNumber.toString()

        // Then
        assertTrue(toString.contains("SmsOtpNumber"))
        assertTrue(toString.contains("number"))
        assertTrue(toString.contains("starredNumber"))
    }
}
