package com.divpundir.template.android.login

import com.divpundir.template.android.core.network.makeApiRequest
import com.divpundir.template.android.core.preferences.AccountPreference
import com.divpundir.template.android.login.api.LoginApi
import com.divpundir.template.android.login.api.LoginWithPassword
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class LoginRepository @Inject constructor(
    private val accPrefManager: AccountPreference.Manager,
    private val loginApi: LoginApi
) {
    suspend fun loginWithPassword(
        request: LoginWithPassword.Request
    ) = makeApiRequest {
        loginApi.loginWithPassword(request)
    }

    fun createSession(
        authToken: String,
        userId: Long,
        userName: String,
    ) {
        accPrefManager.createSession(
            authToken = authToken,
            id = userId,
            name = userName,
        )
    }
}
