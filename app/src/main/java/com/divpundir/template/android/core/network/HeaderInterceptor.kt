package com.divpundir.template.android.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

class HeaderInterceptor(
    private val name: String,
    private val value: String
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request()
            .newBuilder()
            .header(name, value)
            .build()

        return chain.proceed(newRequest)
    }
}
