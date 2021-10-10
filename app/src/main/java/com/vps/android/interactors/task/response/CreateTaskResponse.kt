package com.vps.android.interactors.task.response

data class CreateTaskResponse<T>(
    val error: String? = null,
    val success: String? = null,
    val task: T?,
)
