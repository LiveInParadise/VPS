package com.vps.android.interactors.auth.request

data class ProfileRegRequest(
    val lastName: String,
    val firstName: String,
    val number: String,
    val password: String,
    val email: String,
)
