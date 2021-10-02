package com.vps.android.interactors.auth

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.repository.AuthRepository
import com.vps.android.interactors.auth.response.TokenObj

class AuthInteractor(
    private val repository: AuthRepository,
) {

    suspend fun login(pinCode: String): RequestResult<TokenObj> =
        repository.login(pinCode)
}
