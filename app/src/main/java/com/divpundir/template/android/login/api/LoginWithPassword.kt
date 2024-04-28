package com.divpundir.template.android.login.api

import kotlinx.serialization.Serializable

object LoginWithPassword {

    @Serializable
    data class Request(
        val email: String,
        val password: String
    )

    @Serializable
    data class Response(
        val authToken: String,
        val userId: Long,
        val userName: String
    )
}
