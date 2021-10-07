package com.vps.android.interactors.task.request

data class CreateTaskRequest(
    val task_type_id: Int,
    val loading_place_id: Int? = null,
    val unloading_place_id: Int? = null,
    val goods_id: Int? = null,
    val selected_mechanisms: List<Int>? = null,
)
