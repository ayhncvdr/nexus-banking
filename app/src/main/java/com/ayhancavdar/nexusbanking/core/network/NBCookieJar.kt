/*
 * Copyright 2025 Ayhan Cavdar. All Rights Reserved.
 *
 * Save to the extent permitted by law, you may not use, copy, modify,
 * distribute or create derivative works of this material or any part
 * of it without the prior written consent of Ayhan Cavdar.
 * Any reproduction of this material must contain this notice.
 */

package com.ayhancavdar.nexusbanking.core.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class NBCookieJar : CookieJar {
    private val cookies = mutableMapOf<String, Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        synchronized(this.cookies) {
            cookies.forEach { cookie ->
                if (cookie.matches(url)) {
                    val key = "${cookie.name}-${cookie.domain}-${cookie.path}"
                    this.cookies[key] = cookie
                }
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        synchronized(cookies) {
            val now = System.currentTimeMillis()
            return cookies.values.filter { cookie ->
                cookie.matches(url) && (cookie.expiresAt == Long.MAX_VALUE || cookie.expiresAt > now)
            }
        }
    }
}
