package com.vps.android.interactors.auth

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.repository.AuthRepository

class AuthInteractor(
    private val repository: AuthRepository,
) {

    suspend fun checkAuth(): RequestResult<Boolean> =
        repository.checkAuth()
}
