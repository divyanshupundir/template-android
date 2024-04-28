package com.divpundir.template.android.login

import com.divpundir.template.android.core.ui.UiText

sealed interface UiEvent {

    data class LoginSuccess(val message: UiText) : UiEvent

    data class LoginFailure(val message: UiText) : UiEvent
}