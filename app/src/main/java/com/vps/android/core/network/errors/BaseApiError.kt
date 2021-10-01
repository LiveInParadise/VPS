package com.vps.android.core.network.errors

class BaseApiError(
    val code: Int,
    val errorMessage: String
) : Throwable()
