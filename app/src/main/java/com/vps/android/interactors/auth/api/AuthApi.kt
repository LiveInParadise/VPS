package com.vps.android.interactors.auth.api

import com.vps.android.core.network.base.BaseResponseObj
import retrofit2.http.GET

interface AuthApi {

    /**
     * Валидация авторизации по JWT
     **/
    @GET("/auth/check")
    suspend fun checkAuth(): BaseResponseObj<Boolean>

//    /**
//     * Регистрация
//     **/
//    @POST("auth/register")
//    suspend fun auth(
//        @Body request: ProfileAuthRequest,
//    ): BaseResponseObj<ProfileAuthResponseObj>
}
