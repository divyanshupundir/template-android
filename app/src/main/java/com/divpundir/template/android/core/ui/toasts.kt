package com.divpundir.template.android.core.ui

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import es.dmoral.toasty.Toasty

fun Context.toastNormal(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.normal(this, message, duration).show()
}

fun Context.toastNormal(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.normal(this, message, duration).show()
}

fun Context.toastError(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.error(this, message, duration, false).show()
}

fun Context.toastError(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toasty.error(this, message, duration, false).show()
}
