package com.vps.android.interactors.auth.api

import com.vps.android.interactors.auth.request.PinAuthRequest
import com.vps.android.interactors.auth.response.AuthBaseResponseObj
import com.vps.android.interactors.auth.response.TokenObj
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    /**
     * Pin validation
     **/
    @POST("/api/auth/login")
    suspend fun login(
        @Body request: PinAuthRequest,
    ): AuthBaseResponseObj<TokenObj>

//    /**
//     * Регистрация
//     **/
//    @POST("auth/register")
//    suspend fun auth(
//        @Body request: ProfileAuthRequest,
//    ): BaseResponseObj<ProfileAuthResponseObj>
}
