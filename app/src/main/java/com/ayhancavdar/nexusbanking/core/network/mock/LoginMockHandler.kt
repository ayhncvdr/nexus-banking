/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.network.mock

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val STATUS_CODE_OK = 200

object LoginMockHandler {
    fun handle(request: Request): Response {
        val password = extractPasswordFromRequest(request)
        val mockResponse = if (password == "654321") {
            MockResponses.LOGIN_SUCCESS
        } else {
            MockResponses.LOGIN_ERROR
        }
        return Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(STATUS_CODE_OK)
            .message("OK")
            .body(mockResponse.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun extractPasswordFromRequest(request: Request): String? {
        return try {
            val requestBody = request.body
            val buffer = okio.Buffer()
            requestBody?.writeTo(buffer)
            val requestBodyString = buffer.readUtf8()

            val json = Json { ignoreUnknownKeys = true }
            val jsonElement = json.parseToJsonElement(requestBodyString)
            jsonElement.jsonObject["password"]?.jsonPrimitive?.content
        } catch (e: kotlinx.serialization.SerializationException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
