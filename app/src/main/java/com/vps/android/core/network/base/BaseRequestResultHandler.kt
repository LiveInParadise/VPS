package com.vps.android.core.network.base

import com.google.gson.Gson
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.network.errors.BaseApiError
import com.vps.android.core.network.errors.ErrorBody
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.core.network.ext.wrapResult
import com.vps.android.interactors.auth.response.AuthBaseResponseObj
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRequestResultHandler(
    protected val dispatchersProvider: DispatchersProvider,
    private val errorMapper: ErrorMapper,
    private val gson: Gson,
) {

    suspend fun <T, D> call(action: suspend () -> T): RequestResult<D>
            where T : AuthBaseResponseObj<D> =
        withContext(dispatchersProvider.io()) {
            when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    when (val errorData = result.error) {
                        is HttpException -> {
                            val error = errorData.response()?.errorBody()?.string()
                            val errorBody = gson.fromJson(error, ErrorBody::class.java)
                            RequestResult.Error<D>(Throwable(errorBody.error))
                        }
                        else -> {
                            RequestResult.Error<D>(Throwable(errorData.message))
                        }
                    }
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.success != null -> RequestResult.Success(response.success)
                        else -> RequestResult.Error<D>(Throwable(response.error))
                    }
                }
            }
        }

    suspend fun <R, T, D> callAndMap(action: suspend () -> R): RequestResult<D>
            where R : BaseResponseObj<T>,
                  T : Transformable<D> =
        withContext(dispatchersProvider.io()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    val mappedError = errorMapper.mapError<T, R>(result.error, null)
                    RequestResult.Error(mappedError)
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.code != CODE_SUCCESS -> {
                            val mappedError = errorMapper.mapError<T, R>(
                                BaseApiError(
                                    response.code,
                                    response.error ?: ""
                                )
                            )
                            RequestResult.Error(mappedError)
                        }
                        response.data != null -> RequestResult.Success(response.data.transform())
                        else -> RequestResult.Error(
                            errorMapper.mapError<T, R>(
                                BaseApiError(CODE_ERROR, "")
                            )
                        )
                    }
                }
            }
        }

    suspend fun <R, T, D> callAndMapList(action: suspend () -> R): RequestResult<List<D>>
            where R : BaseResponseObj<List<T>>,
                  T : Transformable<D> =
        withContext(dispatchersProvider.default()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    val mappedError = errorMapper.mapError<List<T>, R>(result.error, null)
                    RequestResult.Error(mappedError)
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.code != CODE_SUCCESS -> {
                            val mappedError = errorMapper.mapError<List<T>, R>(
                                BaseApiError(
                                    response.code,
                                    response.error ?: ""
                                )
                            )
                            RequestResult.Error(mappedError)
                        }
                        response.data != null -> RequestResult.Success(response.data.transform())
                        else -> RequestResult.Error(
                            errorMapper.mapError<List<T>, R>(
                                BaseApiError(CODE_ERROR, "")
                            )
                        )
                    }
                }
            }
        }

}
