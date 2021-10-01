package com.vps.android.interactors.auth.repository

import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.local.PrefManager
import com.vps.android.core.network.base.BaseRequestResultHandler
import com.vps.android.core.network.base.RequestResult
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.core.network.errors.NetworkErrorBus
import com.vps.android.interactors.auth.api.AuthApi

class AuthRepositoryImpl(
    dispatchersProvider: DispatchersProvider,
    errorMapper: ErrorMapper,
    networkErrorBus: NetworkErrorBus,
    private val api: AuthApi,
    private val pref: PrefManager,
) : AuthRepository, BaseRequestResultHandler(dispatchersProvider, errorMapper, networkErrorBus) {

    override suspend fun checkAuth(): RequestResult<Boolean> =
        call {
            api.checkAuth()
        }
}
