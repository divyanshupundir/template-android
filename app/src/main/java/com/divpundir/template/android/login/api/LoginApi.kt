package com.divpundir.template.android.login.api

import com.slack.eithernet.ApiResult
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
    suspend fun loginWithPassword(
        @Body request: LoginWithPassword.Request
    ): ApiResult<LoginWithPassword.Response, Unit>
}
