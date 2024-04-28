package com.divpundir.template.android.login

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.divpundir.template.android.R
import com.divpundir.template.android.core.ui.AppTheme
import com.divpundir.template.android.core.ui.toastError
import com.divpundir.template.android.core.ui.toastNormal
import com.divpundir.template.android.core.util.isValidEmail
import com.divpundir.template.android.core.util.isValidPassword
import com.divpundir.template.android.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val email by viewModel.email.collectAsStateWithLifecycle()
                val password by viewModel.password.collectAsStateWithLifecycle()

                LoginActivityScreen(
                    uiState = uiState,
                    email = email,
                    onEmailChange = viewModel::setEmail,
                    password = password,
                    onPasswordChange = viewModel::setPassword,
                    loginWithPassword = viewModel::loginWithPassword
                )
            }
        }

        collectUiEvent()
    }

    private fun collectUiEvent() {
        lifecycleScope.launch {
            viewModel
                .uiEvent
                .flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        is UiEvent.LoginSuccess -> {
                            toastNormal(it.message)
                            HomeActivity.start(this@LoginActivity)
                        }

                        is UiEvent.LoginFailure -> {
                            toastError(it.message)
                        }
                    }
                }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun LoginActivityPreview() {
    AppTheme {
        LoginActivityScreen(
            uiState = UiState.Normal,
            email = "abcd@efgh.com",
            onEmailChange = {},
            password = "12345678",
            onPasswordChange = {},
            loginWithPassword = {}
        )
    }
}

@Composable
private fun LoginActivityScreen(
    uiState: UiState,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    loginWithPassword: () -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val uiEnabled = when (uiState) {
        UiState.Normal -> true
        UiState.Loading -> false
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(32.dp))

        InputTextField(
            modifier = Modifier.width(360.dp),
            label = { Text(stringResource(R.string.label_email)) },
            enabled = uiEnabled,
            isError = email.isNotEmpty() && !email.trim().isValidEmail(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            value = email,
            onValueChange = onEmailChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        InputTextField(
            modifier = Modifier.width(360.dp),
            label = { Text(stringResource(R.string.label_password)) },
            enabled = uiEnabled,
            isError = password.isNotEmpty() &&
                !password.trim().isValidPassword(minLength = LoginViewModel.PASSWORD_MIN_LENGTH),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = painterResource(
                    id = if (isPasswordVisible) R.drawable.ic_password_visible_24dp else R.drawable.ic_password_invisible_24dp
                )
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(painter = image, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            value = password,
            onValueChange = onPasswordChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        SolidButton(
            modifier = Modifier
                .width(360.dp)
                .height(48.dp),
            enabled = uiEnabled,
            onClick = loginWithPassword
        ) {
            Text(text = stringResource(R.string.label_login))
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiEnabled) {
            Spacer(modifier = Modifier.height(32.dp))
        } else {
            CircularProgressIndicator(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        label = label,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = MaterialTheme.colorScheme.error
        )
    )
}

@Composable
private fun SolidButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        content = content
    )
}
