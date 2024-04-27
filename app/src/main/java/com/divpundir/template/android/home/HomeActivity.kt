package com.divpundir.template.android.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.divpundir.template.android.core.ui.AppTheme

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

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                HomeActivityScreen(viewModel)
            }
        }
    }
}

@Composable
private fun HomeActivityScreen(viewModel: HomeViewModel) {}
