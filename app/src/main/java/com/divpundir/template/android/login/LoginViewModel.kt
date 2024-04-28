package com.divpundir.template.android.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divpundir.template.android.core.util.isValidEmail
import com.divpundir.template.android.core.util.isValidPassword
import com.divpundir.template.android.login.api.LoginWithPassword
import com.slack.eithernet.successOrNothing
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository
) : ViewModel() {
    companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Normal)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun loginWithPassword() {
        val email = email.value.trim()
        val password = password.value.trim()

        if (!email.isValidEmail() && !password.isValidPassword(minLength = PASSWORD_MIN_LENGTH)) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.LoginFailure(
                        "Invalid credentials"
                    )
                )
            }
            return
        }

        if (!email.isValidEmail()) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.LoginFailure(
                        "Invalid email"
                    )
                )
            }
            return
        }

        if (!password.isValidPassword(minLength = PASSWORD_MIN_LENGTH)) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.LoginFailure(
                        "Invalid password"
                    )
                )
            }
            return
        }

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val result = repo.loginWithPassword(
                LoginWithPassword.Request(
                    email = email,
                    password = password
                )
            ).successOrNothing {
                _uiState.value = UiState.Normal
                _uiEvent.emit(
                    UiEvent.LoginFailure(
                        "Login failure"
                    )
                )
                return@launch
            }

            repo.createSession(
                authToken = result.authToken,
                userId = result.userId,
                userName = result.userName,
            )

            _uiEvent.emit(
                UiEvent.LoginSuccess(
                    "Welcome, ${result.userName}!"
                )
            )
        }
    }
}
