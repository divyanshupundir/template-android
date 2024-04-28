package com.divpundir.template.android.login

sealed interface UiState {

    data object Normal : UiState

    data object Loading : UiState
}