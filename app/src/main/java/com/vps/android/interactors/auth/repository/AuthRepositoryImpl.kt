package com.vps.android.interactors.auth.repository

import com.google.gson.Gson
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.local.PrefManager
import com.vps.android.core.network.base.BaseRequestResultHandler
import com.vps.android.core.network.base.RequestResult
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.interactors.auth.api.AuthApi
import com.vps.android.interactors.auth.request.PinAuthRequest
import com.vps.android.interactors.auth.response.TokenObj

class AuthRepositoryImpl(
    dispatchersProvider: DispatchersProvider,
    errorMapper: ErrorMapper,
    gson: Gson,
    private val api: AuthApi,
    private val pref: PrefManager,
) : AuthRepository, BaseRequestResultHandler(dispatchersProvider, errorMapper, gson) {

    override suspend fun login(pinCode: String): RequestResult<TokenObj> =
        call {
            val request = PinAuthRequest(pin = pinCode.toIntOrNull() ?: 0, device_id = "a4a9498asd498asd489")
            api.login(request)
        }.let { response ->
            if (response is RequestResult.Success) {
                pref.token = response.data.token ?: ""
            }
            return@let response
        }

    override suspend fun logout(): RequestResult<String> =
        call {
            val response = api.logout()
            pref.clearAll()
            response
        }
}
