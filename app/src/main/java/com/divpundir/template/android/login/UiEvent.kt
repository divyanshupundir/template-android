package com.divpundir.template.android.login

sealed interface UiEvent {

    data class LoginSuccess(val message: String) : UiEvent

    data class LoginFailure(val message: String) : UiEvent
}