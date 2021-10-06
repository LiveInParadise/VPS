package com.vps.android.core.network.errors

import com.vps.android.core.network.base.BaseResponseObj
import com.vps.android.interactors.mechanism.response.MechanismSelectResponse
import java.net.UnknownHostException

class DefaultErrorMapper : ErrorMapper {

    override fun <T, D : BaseResponseObj<T>> mapError(error: Throwable, data: D?): Throwable {
        return when (error) {
            is BaseApiError -> handleBaseApiError(error, data)
            is NoNetworkError -> NoNetworkError()
            is UnknownHostException -> NoNetworkError()
            else -> UnknownError()
        }
    }

    override fun <T, D : MechanismSelectResponse<T>> mapMechanismError(error: Throwable, data: D?): Throwable {
        return when (error) {
            is BaseApiError -> handleBaseApiError(error, data)
            is NoNetworkError -> NoNetworkError()
            is UnknownHostException -> NoNetworkError()
            else -> UnknownError()
        }
    }

    private fun <T, D : BaseResponseObj<T>> handleBaseApiError(
        error: BaseApiError,
        data: D?
    ): Throwable {
        val message = error.errorMessage
        val code = error.code

        return when (code) {
            // код -> возвращаемая ошибка
            else -> UnknownError()
        }
    }

    private fun <T, D : MechanismSelectResponse<T>> handleBaseApiError(
        error: BaseApiError,
        data: D?
    ): Throwable {
        val message = error.errorMessage
        val code = error.code

        return when (code) {
            // код -> возвращаемая ошибка
            else -> UnknownError()
        }
    }
}

