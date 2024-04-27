package com.divpundir.template.android.core.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Inject
import javax.inject.Singleton

object AccountPreference {

    private const val PREF_NAME = "account_pref"
    private const val KEY_PREFIX = PREF_NAME + "_key_"

    private const val KEY_AUTH_TOKEN = KEY_PREFIX + "auth_token"
    private const val KEY_ID = KEY_PREFIX + "id"
    private const val KEY_EMAIL = KEY_PREFIX + "email"

    private const val NULL_ID = -1L

    @Singleton
    class Manager @Inject constructor(
        application: Application
    ) {
        private val preferences: SharedPreferences = try {
            createEncryptedSharedPreferences(application)
        } catch (e: IOException) {
            throw IOException("Cannot create encrypted shared preference", e)
        } catch (e: GeneralSecurityException) {
            throw GeneralSecurityException("Cannot create encrypted shared preference", e)
        }

        @Throws(GeneralSecurityException::class, IOException::class)
        private fun createEncryptedSharedPreferences(context: Context): SharedPreferences {
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            return EncryptedSharedPreferences.create(
                context,
                PREF_NAME,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        val isLoggedIn: Boolean
            get() = id != null

        val authToken: String?
            get() = preferences.getString(KEY_AUTH_TOKEN, null)

        val id: Long?
            get() {
                val id = preferences.getLong(KEY_ID, NULL_ID)
                return if (id == NULL_ID) null else id
            }

        val email: String?
            get() = preferences.getString(KEY_EMAIL, null)

        fun createSession(authToken: String, id: Long, email: String) {
            preferences.edit {
                putString(KEY_AUTH_TOKEN, authToken)
                putLong(KEY_ID, id)
                putString(KEY_EMAIL, email)
            }
        }

        fun logout() {
            preferences.edit(commit = true) {
                clear()
            }
        }
    }
}
