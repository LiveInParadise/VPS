package com.vps.android.core.network.base

data class BaseResponseObj<T>(
    val error: String? = null,
    val success: T?
)
