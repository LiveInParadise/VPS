package com.vps.android.interactors.task

import com.vps.android.interactors.task.repository.TaskRepository
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.interactors.task.request.UpdateTaskRequest

class TaskInteractor(
    private val repository: TaskRepository,
) {

    suspend fun createTask(request: CreateTaskRequest) = repository.createTask(request)

    suspend fun getTaskTypesList() = repository.getTaskTypesList()

    suspend fun getTaskList() = repository.getTaskList()

    suspend fun startTask(taskId: Int, request: StartTaskRequest) = repository.startTask(taskId, request)

    suspend fun editTask(taskId: Int, request: UpdateTaskRequest) = repository.editTask(taskId, request)

    suspend fun stopTask(taskId: Int, request: StopTaskRequest) = repository.stopTask(taskId, request)

    suspend fun selectMechanism(mechanismId: Int) = repository.selectMechanism(mechanismId)

    suspend fun getPlacesList() = repository.getPlacesList()

    suspend fun getGoodsList() = repository.getGoodsList()
}
