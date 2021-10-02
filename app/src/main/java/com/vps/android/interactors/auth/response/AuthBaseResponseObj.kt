package com.vps.android.interactors.auth.response

data class AuthBaseResponseObj<T>(
    val error: String? = null,
    val success: T?
)
