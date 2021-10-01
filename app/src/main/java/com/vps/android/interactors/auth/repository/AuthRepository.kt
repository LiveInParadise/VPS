package com.vps.android.interactors.auth.repository

import com.vps.android.core.network.base.RequestResult

interface AuthRepository {

    suspend fun checkAuth(): RequestResult<Boolean>
}
