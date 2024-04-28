package com.divpundir.template.android.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.divpundir.template.android.core.preferences.AccountPreference
import com.divpundir.template.android.core.ui.AppTheme
import com.divpundir.template.android.login.LoginActivity
import javax.inject.Inject

class HomeActivity : ComponentActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }

    @Inject
    internal lateinit var accPrefManager: AccountPreference.Manager

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!accPrefManager.isLoggedIn) {
            LoginActivity.start(this)
            finish()
            return
        }

        setContent {
            AppTheme {
                HomeActivityScreen()
            }
        }
    }
}

@Preview
@Composable
private fun HomeActivityPreview() {
    AppTheme {
        HomeActivityScreen()
    }
}

@Composable
private fun HomeActivityScreen() {}
