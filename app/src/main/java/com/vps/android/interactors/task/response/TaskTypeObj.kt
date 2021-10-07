package com.vps.android.interactors.task.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.domain.task.TaskTypeItem

data class TaskTypeObj(
    val id: Int,
    val name: String,
    val mechanism_type_id: Int,
) : Transformable<TaskTypeItem> {

    override fun transform(): TaskTypeItem =
        TaskTypeItem(
            id = id,
            name = name,
            mechanismTypeId = mechanism_type_id,
        )
}
