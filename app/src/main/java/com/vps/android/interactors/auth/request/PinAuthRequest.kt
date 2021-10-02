package com.vps.android.interactors.auth.request

data class PinAuthRequest(
    val pin: Int,
    val device_id: String
)
