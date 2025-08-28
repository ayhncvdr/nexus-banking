/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.network

import com.ayhancavdar.nexusbanking.core.network.mock.LoginMockHandler
import com.ayhancavdar.nexusbanking.core.network.mock.OtpMockHandler
import okhttp3.Interceptor
import okhttp3.Response

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        return when {
            path.contains("login") -> LoginMockHandler.handle(request)
            path.contains("otp") -> OtpMockHandler.handle(request)
            else -> chain.proceed(request)
        }
    }
}
