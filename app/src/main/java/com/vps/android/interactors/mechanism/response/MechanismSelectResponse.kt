package com.vps.android.interactors.mechanism.response

data class MechanismSelectResponse<T>(
    val error: String? = null,
    val success: String? = null,
    val mechanism: T?,
)
