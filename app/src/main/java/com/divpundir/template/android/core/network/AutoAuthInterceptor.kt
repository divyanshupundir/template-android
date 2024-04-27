package com.divpundir.template.android.core.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AutoAuthInterceptor(
    private val tokenProvider: () -> String?
) : Interceptor {

    companion object {
        private const val KEY_AUTHORIZATION = "Authorization"

        private const val KEY_AUTO_AUTH = "Auto-Auth"
        private const val VALUE_AUTO_AUTH_TRUE = "true"

        const val HEADER = "$KEY_AUTO_AUTH: $VALUE_AUTO_AUTH_TRUE"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = if (request.header(KEY_AUTO_AUTH) == VALUE_AUTO_AUTH_TRUE) {
            val authToken = tokenProvider.invoke() ?: throw IOException("Auth token not available")

            request.newBuilder()
                .removeHeader(KEY_AUTO_AUTH)
                .header(KEY_AUTHORIZATION, authToken)
                .build()
        } else {
            request
        }

        return chain.proceed(newRequest)
    }
}
