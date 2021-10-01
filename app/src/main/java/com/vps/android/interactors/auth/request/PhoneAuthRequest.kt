package com.vps.android.interactors.auth.request

data class PhoneAuthRequest(
    val phone: String,
    val deviceToken: String? = null
)
