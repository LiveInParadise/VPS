package com.vps.android.interactors.auth.repository

import com.vps.android.core.network.base.RequestResult
import com.vps.android.interactors.auth.response.TokenObj

interface AuthRepository {

    suspend fun login(pinCode: String): RequestResult<TokenObj>
}
