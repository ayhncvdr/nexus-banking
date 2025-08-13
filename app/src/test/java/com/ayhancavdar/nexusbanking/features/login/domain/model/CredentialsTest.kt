/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.features.login.domain.model

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CredentialsTest {

    @Test
    fun `credentials creation - should create with valid username and password`() {
        // Given
        val username = "testuser"
        val password = "123456"

        // When
        val credentials = Credentials(username = username, password = password)

        // Then
        assertEquals(username, credentials.username)
        assertEquals(password, credentials.password)
    }

    @Test
    fun `credentials creation - should create with empty values`() {
        // Given
        val credentials = Credentials(username = "", password = "")

        // Then
        assertEquals("", credentials.username)
        assertEquals("", credentials.password)
    }

    @Test
    fun `credentials equality - should be equal when username and password match`() {
        // Given
        val credentials1 = Credentials(username = "test123", password = "654321")
        val credentials2 = Credentials(username = "test123", password = "654321")

        // Then
        assertEquals(credentials1, credentials2)
        assertEquals(credentials1.hashCode(), credentials2.hashCode())
    }

    @Test
    fun `credentials equality - should not be equal when username differs`() {
        // Given
        val credentials1 = Credentials(username = "test123", password = "654321")
        val credentials2 = Credentials(username = "different", password = "654321")

        // Then
        assertNotEquals(credentials1, credentials2)
    }

    @Test
    fun `credentials equality - should not be equal when password differs`() {
        // Given
        val credentials1 = Credentials(username = "test123", password = "654321")
        val credentials2 = Credentials(username = "test123", password = "different")

        // Then
        assertNotEquals(credentials1, credentials2)
    }

    @Test
    fun `credentials equality - should not be equal when both username and password differ`() {
        // Given
        val credentials1 = Credentials(username = "test123", password = "654321")
        val credentials2 = Credentials(username = "different", password = "different")

        // Then
        assertNotEquals(credentials1, credentials2)
    }

    @Test
    fun `credentials toString - should contain class name but not expose sensitive data`() {
        // Given
        val credentials = Credentials(username = "testuser", password = "secret123")

        // When
        val toString = credentials.toString()

        // Then
        assertTrue(toString.contains("Credentials"))
        // Ensure sensitive data is not exposed in toString
        assertFalse(toString.contains("secret123"))
    }

    @Test
    fun `credentials copy - should create new instance with modified values`() {
        // Given
        val original = Credentials(username = "original", password = "password123")

        // When
        val copied = original.copy(username = "modified")

        // Then
        assertEquals("modified", copied.username)
        assertEquals("password123", copied.password)
        assertNotEquals(original, copied)
    }

    @Test
    fun `credentials copy - should create identical instance when no parameters changed`() {
        // Given
        val original = Credentials(username = "testuser", password = "123456")

        // When
        val copied = original.copy()

        // Then
        assertEquals(original, copied)
        assertEquals(original.username, copied.username)
        assertEquals(original.password, copied.password)
    }

    @Test
    fun `credentials destructuring - should allow component access`() {
        // Given
        val credentials = Credentials(username = "testuser", password = "123456")

        // When
        val (username, password) = credentials

        // Then
        assertEquals("testuser", username)
        assertEquals("123456", password)
    }
}
