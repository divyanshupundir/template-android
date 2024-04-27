package com.divpundir.template.android.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {

    fun asString(context: Context): String

    @Composable
    fun asString(): String

    @Immutable
    @JvmInline
    value class Raw(private val value: String) : UiText {

        override fun asString(context: Context) = value

        @Composable
        override fun asString() = value
    }

    @Immutable
    class Res(@StringRes private val value: Int, private vararg val args: Any) : UiText {

        override fun asString(context: Context) = context.getString(value, *args)

        @Composable
        override fun asString() = stringResource(id = value, *args)
    }

    companion object {
        val Empty: UiText = Raw("")
    }
}

fun String.asUiText(): UiText = UiText.Raw(this)

fun UiText?.orEmpty(): UiText = this ?: UiText.Empty
