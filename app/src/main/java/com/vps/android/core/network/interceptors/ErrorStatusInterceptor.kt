package com.vps.android.core.network.interceptors

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.vps.android.core.ext.bodySnapshotUtf8
import com.vps.android.core.network.errors.ErrorBody
import com.vps.android.core.network.errors.RequestError
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent

class ErrorStatusInterceptor(
    private val moshi: Moshi
) : Interceptor, KoinComponent {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val res = chain.proceed(originalRequest)

        val bodyObj: ErrorBody? = try {
            val str = res.bodySnapshotUtf8 ?: ""
            moshi.adapter(ErrorBody::class.java).fromJson(str)
        } catch (e: JsonEncodingException) {
            ErrorBody(0, "")
        }

        // проверка code из боди
//        val errMessage = bodyObj?.message
//        if (bodyObj?.code != CODE_SUCCESS) {
//            when (bodyObj?.code) {
//                CODE_ERROR -> throw ApiError.GeneralError(errMessage)
//                CODE_NO_DATA -> throw ApiError.NoDataError(errMessage)
//                CODE_TOKEN_ERROR -> throw ApiError.TokenError(errMessage)
//                CODE_SMS_SEND_ERROR -> throw ApiError.SmsSendError(errMessage)
//                else -> throw ApiError.UnknownError(errMessage)
//            }
//        }

        if (res.isSuccessful) return res

        // проверка кода ответа
        val errMessage = bodyObj?.message
        when (res.code) {
            400 -> throw RequestError.BadRequest(errMessage)
            401 -> throw RequestError.Unauthorized(errMessage)
            403 -> throw RequestError.Forbidden(errMessage)
            404 -> throw RequestError.NotFound(errMessage)
            500 -> throw RequestError.InternalServerError(errMessage)
            else -> throw RequestError.UnknownError(errMessage)
        }
    }

}
