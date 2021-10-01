package com.vps.android.core.network.interceptors

import com.vps.android.core.utils.NetworkMonitor
import com.vps.android.core.network.errors.NoNetworkError
import okhttp3.Interceptor
import okhttp3.Response

class NetworkStatusInterceptor(
    private val networkMonitor: NetworkMonitor
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val isConnected = networkMonitor
            .connection
            .value
            ?.isConnected
            ?: false
        if (!isConnected) {
            throw NoNetworkError()
        }
        return chain.proceed(chain.request())
    }
}
