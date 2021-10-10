package com.vps.android.core.network.base

import com.google.gson.Gson
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.network.errors.ErrorBody
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.core.network.ext.wrapResult
import com.vps.android.interactors.mechanism.response.MechanismSelectResponse
import com.vps.android.interactors.task.response.CreateTaskResponse
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRequestResultHandler(
    protected val dispatchersProvider: DispatchersProvider,
    private val errorMapper: ErrorMapper,
    private val gson: Gson,
) {

    suspend fun <T, D> call(action: suspend () -> T): RequestResult<D>
            where T : BaseResponseObj<D> =
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
                        response.success != null -> RequestResult.Success(response.success.transform())
                        else -> RequestResult.Error<D>(Throwable(response.error))
                    }
                }
            }
        }

    suspend fun <R, T, D> callAndMapTask(action: suspend () -> R): RequestResult<D>
            where R : CreateTaskResponse<T>,
                  T : Transformable<D> =
        withContext(dispatchersProvider.io()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    val mappedError = errorMapper.mapTaskError<T, R>(result.error, null)
                    RequestResult.Error(mappedError)
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.task != null -> RequestResult.Success(response.task.transform())
                        else -> RequestResult.Error<D>(Throwable(response.error))
                    }
                }
            }
        }

    suspend fun <R, T, D> callAndMapMechanism(action: suspend () -> R): RequestResult<D>
            where R : MechanismSelectResponse<T>,
                  T : Transformable<D> =
        withContext(dispatchersProvider.io()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    val mappedError = errorMapper.mapMechanismError<T, R>(result.error, null)
                    RequestResult.Error(mappedError)
                }
                is RequestResult.Success -> {
                    val response = result.data
                    when {
                        response.mechanism != null -> RequestResult.Success(response.mechanism.transform())
                        else -> RequestResult.Error<D>(Throwable(response.error))
                    }
                }
            }
        }

    suspend fun <T, D> callAndMapList(action: suspend () -> List<T>): RequestResult<List<D>>
            where T : Transformable<D> =
        withContext(dispatchersProvider.default()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    RequestResult.Success(listOf())
                }
                is RequestResult.Success -> {
                    RequestResult.Success(result.data.transform())
                }
            }
        }

    suspend fun <T, D> callAndMapListBase(action: suspend () -> List<T>): List<D>
            where T : Transformable<D> =
        withContext(dispatchersProvider.default()) {
            return@withContext when (val result = wrapResult { action() }) {
                is RequestResult.Error -> {
                    listOf()
                }
                is RequestResult.Success -> {
                    result.data.transform()
                }
            }
        }

}
