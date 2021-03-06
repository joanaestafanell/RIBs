package com.badoo.ribs.example.auth

import android.content.SharedPreferences

interface AuthStateStorage {
    fun save(state: AuthState)
    fun restore(): AuthState
}

class PreferencesAuthStateStorage(
    private val preferences: SharedPreferences
) : AuthStateStorage {

    override fun save(state: AuthState) {
        when (state) {
            is AuthState.Unauthenticated -> preferences.edit()
                .clear()
                .apply()
            is AuthState.Anonymous -> preferences.edit()
                .clear()
                .putBoolean(ANONYMOUS_AUTH_KEY, true)
                .apply()
            is AuthState.Authenticated -> preferences.edit()
                .clear()
                .putString(ACCESS_TOKEN_KEY, state.accessToken)
                .apply()
        }
    }

    override fun restore(): AuthState {
        val accessToken = preferences.getString(ACCESS_TOKEN_KEY, null)
        val isAnonymousAuth = preferences.getBoolean(ANONYMOUS_AUTH_KEY, false)
        return when {
            accessToken != null -> AuthState.Authenticated(accessToken)
            isAnonymousAuth -> AuthState.Anonymous
            else -> AuthState.Unauthenticated
        }
    }

    private companion object {
        const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        const val ANONYMOUS_AUTH_KEY = "ANONYMOUS_AUTH"
    }
}
