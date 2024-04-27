package com.divpundir.template.android.core.util

import androidx.core.util.PatternsCompat

fun String.isValidEmail(): Boolean =
    if (this.isBlank()) false
    else PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(minLength: Int): Boolean =
    if (this.isBlank()) false
    else this.length >= minLength
