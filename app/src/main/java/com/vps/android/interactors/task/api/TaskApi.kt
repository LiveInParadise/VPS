package com.vps.android.interactors.task.api

import com.vps.android.core.network.base.BaseResponseObj
import com.vps.android.interactors.task.request.CreateTaskRequest
import com.vps.android.interactors.task.request.StartTaskRequest
import com.vps.android.interactors.task.request.StopTaskRequest
import com.vps.android.interactors.task.request.UpdateTaskRequest
import com.vps.android.interactors.task.response.TaskInfoObj
import com.vps.android.interactors.task.response.TaskTypeObj
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {

    /**
     * Create task
     **/
    @POST("/api/task/create")
    suspend fun createTask(
        @Body request: CreateTaskRequest,
    ): BaseResponseObj<String>

    /**
     * Get list of task types
     **/
    @GET("/api/task/types")
    suspend fun getTaskTypesList(): List<TaskTypeObj>

    /**
     * Get task list
     **/
    @GET("/api/task/list")
    suspend fun getTaskList(): List<TaskInfoObj>

    /**
     * Start task
     **/
    @POST("/api/task/start/{id}")
    suspend fun startTask(
        @Path("id") taskId: Int,
        @Body request: StartTaskRequest,
    ): BaseResponseObj<String>

    /**
     * Edit task
     **/
    @POST("/api/task/edit/{id}")
    suspend fun editTask(
        @Path("id") taskId: Int,
        @Body request: UpdateTaskRequest,
    ): BaseResponseObj<String>

    /**
     * Stop task
     **/
    @POST("/api/task/stop/{id}")
    suspend fun stopTask(
        @Path("id") taskId: Int,
        @Body request: StopTaskRequest,
    ): BaseResponseObj<String>

    /**
     * Select mechanism
     **/
    @POST("/api/task/select_mechanism/{id}")
    suspend fun selectMechanism(
        @Path("id") mechanismId: Int,
    ): BaseResponseObj<String>
}
