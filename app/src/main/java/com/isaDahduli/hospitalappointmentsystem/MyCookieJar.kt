package com.isaDahduli.hospitalappointmentsystem

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class MyCookieJar : CookieJar
{
    private val cookieStore: MutableMap<String, MutableList<Cookie>> = HashMap()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>)
    {
        val urlHost = url.host
        if (cookieStore[urlHost] == null)
        {
            cookieStore[urlHost] = ArrayList()
        }
        cookieStore[urlHost]?.addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie>
    {
        val cookies = cookieStore[url.host]?.filter { cookie ->
            cookie.matches(url)
        } ?: emptyList()
        return cookies
    }
}
