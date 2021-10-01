package com.vps.android.core.network.ext

import com.vps.android.core.network.base.RequestResult

inline fun <T> wrapResult(block: () -> T): RequestResult<T> {
    return try {
        RequestResult.Success(block())
    } catch (ex: Exception) {
        RequestResult.Error(ex)
    }
}
