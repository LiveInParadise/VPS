package com.vps.android.core.network.interceptors

import com.vps.android.core.local.PrefManager
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class AuthInterceptor(
    private val pref: PrefManager,
) : Interceptor, KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        pref.token.let { requestBuilder.addHeader("Authorization", "Bearer $it") }

        return chain.proceed(requestBuilder.build())
    }

}
