package com.vps.android.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class ErrorStatusInterceptor() : Interceptor, KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val res = chain.proceed(originalRequest)

        return res
    }

}
