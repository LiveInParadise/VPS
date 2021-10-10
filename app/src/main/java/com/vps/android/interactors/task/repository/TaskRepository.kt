package com.vps.android.interactors.task.repository

import com.vps.android.core.network.base.RequestResult
import com.vps.android.domain.task.FullGoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskInfo
import com.vps.android.domain.task.TaskTypeItem
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.interactors.task.request.UpdateTaskRequest

interface TaskRepository {

    suspend fun createTask(request: CreateTaskRequest): RequestResult<TaskInfo>

    suspend fun getTaskTypesList(): List<TaskTypeItem>

    suspend fun getTaskList(): List<TaskInfo>

    suspend fun startTask(taskId: Int, request: StartTaskRequest): RequestResult<String>

    suspend fun editTask(taskId: Int, request: UpdateTaskRequest): RequestResult<TaskInfo>

    suspend fun stopTask(taskId: Int, request: StopTaskRequest): RequestResult<String>

    suspend fun selectMechanism(mechanismId: Int): RequestResult<String>

    suspend fun getPlacesList(): List<PlaceItem>

    suspend fun getGoodsList(): List<FullGoodItem>
}
