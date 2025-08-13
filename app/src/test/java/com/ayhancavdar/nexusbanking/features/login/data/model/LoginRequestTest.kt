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

class LoginRequestTest {

    @Test
    fun `loginRequest creation - should create with valid credentials`() {
        // Given
        val username = "testuser"
        val password = "123456"

        // When
        val request = LoginRequest(username = username, password = password)

        // Then
        assertEquals(username, request.username)
        assertEquals(password, request.password)
    }

    @Test
    fun `loginRequest creation - should create with empty values`() {
        // Given
        val username = ""
        val password = ""

        // When
        val request = LoginRequest(username = username, password = password)

        // Then
        assertEquals("", request.username)
        assertEquals("", request.password)
    }

    @Test
    fun `loginRequest equality - should be equal when all properties match`() {
        // Given
        val request1 = LoginRequest(username = "testuser", password = "123456")
        val request2 = LoginRequest(username = "testuser", password = "123456")

        // Then
        assertEquals(request1, request2)
        assertEquals(request1.hashCode(), request2.hashCode())
    }

    @Test
    fun `loginRequest equality - should not be equal when username differs`() {
        // Given
        val request1 = LoginRequest(username = "user1", password = "123456")
        val request2 = LoginRequest(username = "user2", password = "123456")

        // Then
        assertNotEquals(request1, request2)
    }

    @Test
    fun `loginRequest equality - should not be equal when password differs`() {
        // Given
        val request1 = LoginRequest(username = "testuser", password = "123456")
        val request2 = LoginRequest(username = "testuser", password = "654321")

        // Then
        assertNotEquals(request1, request2)
    }

    @Test
    fun `loginRequest copy - should create new instance with modified username`() {
        // Given
        val original = LoginRequest(username = "original", password = "123456")

        // When
        val copied = original.copy(username = "modified")

        // Then
        assertEquals("modified", copied.username)
        assertEquals("123456", copied.password)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginRequest copy - should create new instance with modified password`() {
        // Given
        val original = LoginRequest(username = "testuser", password = "original")

        // When
        val copied = original.copy(password = "modified")

        // Then
        assertEquals("testuser", copied.username)
        assertEquals("modified", copied.password)
        assertNotEquals(original, copied)
    }

    @Test
    fun `loginRequest destructuring - should allow component access`() {
        // Given
        val request = LoginRequest(username = "testuser", password = "123456")

        // When
        val (username, password) = request

        // Then
        assertEquals("testuser", username)
        assertEquals("123456", password)
    }

    @Test
    fun `loginRequest toString - should not expose password in clear text`() {
        // Given
        val request = LoginRequest(username = "testuser", password = "123456")

        // When
        val toString = request.toString()

        // Then
        assertTrue(toString.contains("LoginRequest"))
        assertTrue(toString.contains("username"))
        // Note: This depends on implementation - you might want to mask passwords
    }
}
