package com.vps.android.interactors.task.repository

import com.google.gson.Gson
import com.vps.android.core.dispatchers.DispatchersProvider
import com.vps.android.core.local.PrefManager
import com.vps.android.core.network.base.BaseRequestResultHandler
import com.vps.android.core.network.base.RequestResult
import com.vps.android.core.network.errors.ErrorMapper
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskInfo
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.task.api.TaskApi
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.interactors.task.request.UpdateTaskRequest

class TaskRepositoryImpl(
    dispatchersProvider: DispatchersProvider,
    errorMapper: ErrorMapper,
    gson: Gson,
    private val api: TaskApi,
    private val pref: PrefManager,
) : TaskRepository, BaseRequestResultHandler(dispatchersProvider, errorMapper, gson) {

    override suspend fun createTask(request: CreateTaskRequest): RequestResult<String> =
        call {
            api.createTask(request)
        }

    override suspend fun getTaskTypesList(): List<TaskTypeItem> =
        callAndMapListBase {
            api.getTaskTypesList()
        }

    override suspend fun getTaskList(): List<TaskInfo> =
        callAndMapListBase {
            api.getTaskList()
        }

    override suspend fun startTask(taskId: Int, request: StartTaskRequest): RequestResult<String> =
        call {
            api.startTask(taskId, request)
        }

    override suspend fun editTask(taskId: Int, request: UpdateTaskRequest): RequestResult<String> =
        call {
            api.editTask(taskId, request)
        }

    override suspend fun stopTask(taskId: Int, request: StopTaskRequest): RequestResult<String> =
        call {
            api.stopTask(taskId, request)
        }

    override suspend fun selectMechanism(mechanismId: Int): RequestResult<String> =
        call {
            api.selectMechanism(mechanismId)
        }

    override suspend fun getPlacesList(): List<PlaceItem> =
        callAndMapListBase {
            api.getPlacesList()
        }

    override suspend fun getGoodsList(): List<FullGoodItem> =
        callAndMapListBase {
            api.getGoodsList()
        }
}
