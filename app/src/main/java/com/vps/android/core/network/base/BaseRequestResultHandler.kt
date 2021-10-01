package com.vps.android.core.network.base

import com.vps.android.core.network.errors.BaseApiError
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.core.network.errors.NetworkErrorBus
import com.vps.android.core.network.ext.wrapResult
import com.vps.android.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.withContext

abstract class BaseRequestResultHandler(
    protected val dispatchersProvider: DispatchersProvider,
    private val errorMapper: ErrorMapper,
    private val errorBus: NetworkErrorBus
) {

    suspend fun <T, D> call(action: suspend () -> T): RequestResult<D>
            where T : BaseResponseObj<D> =
        withContext(dispatchersProvider.io()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    val mappedError = errorMapper.mapError<D, T>(result.error)
                    errorBus.postEvent(mappedError)
                    RequestResult.Error(mappedError)
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.code != CODE_SUCCESS -> {
                            val mappedError = errorMapper.mapError<D, T>(
                                BaseApiError(
                                    response.code,
                                    response.error ?: ""
                                )
                            )
                            errorBus.postEvent(mappedError)
                            RequestResult.Error(mappedError)
                        }
                        response.data != null -> RequestResult.Success(response.data)
                        else -> {
                            val mappedError = errorMapper.mapError<D, T>(
                                BaseApiError(
                                    response.code,
                                    response.error ?: ""
                                )
                            )
                            errorBus.postEvent(mappedError)
                            RequestResult.Error(mappedError)
                        }
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
                    errorBus.postEvent(mappedError)
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
                            errorBus.postEvent(mappedError)
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
                    errorBus.postEvent(mappedError)
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
                            errorBus.postEvent(mappedError)
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
