package com.vps.android.interactors.task.response

import com.vps.android.core.network.base.Transformable
import com.vps.android.core.network.base.transform
import com.vps.android.domain.task.TaskInfo
import com.vps.android.interactors.mechanism.response.MechanismObj

data class TaskInfoObj(
    val id: Int,
    val task_type_id: Int,
    val task_type_name: String,
    val worker_id: Int,
    val worker_name: String?,
    val mechanism_type_id: Int,
    val mechanism_type_name: String,
    val mechanism_id: Int,
    val mechanism_name: String,
    val selected_mechanisms: List<Int>?,
    val goods_id: Int,
    val goods_name: String,
    val loading_place_id: Int,
    val loading_place_name: String,
    val unloading_place_id: Int?,
    val unloading_place_name: String?,
    val distance: Int?,
    val start_time: String?,
    val end_time: String?,
    val date: String?,
    val active: Int,
    val delegated_by_mechanism_id: Int?,
    val delegated_by_task_id: Int?,
    val is_delegated: Int,
    val mechanism_property_value: Int,
    val archived: Int,
    val task_duration: String?,
    val selected_mechanisms_info: List<MechanismObj>,
) : Transformable<TaskInfo> {

    override fun transform(): TaskInfo =
        TaskInfo(
            id = id,
            taskTypeId = task_type_id,
            taskTypeName = task_type_name,
            workerId = worker_id,
            workerName = worker_name,
            mechanismTypeId = mechanism_type_id,
            mechanismTypeName = mechanism_type_name,
            mechanismId = mechanism_id,
            mechanismName = mechanism_name,
            selectedMechanisms = selected_mechanisms,
            goodsId = goods_id,
            goodsName = goods_name,
            loadingPlaceId = loading_place_id,
            loadingPlaceName = loading_place_name,
            unloadingPlaceId = unloading_place_id,
            unloadingPlaceName = unloading_place_name ?: "",
            distance = distance,
            startTime = start_time,
            endTime = end_time,
            date = date,
            active = active,
            delegatedByMechanismId = delegated_by_mechanism_id,
            delegatedByTaskId = delegated_by_task_id,
            isDelegated = is_delegated,
            mechanismPropertyValue = mechanism_property_value,
            archived = archived,
            taskDuration = task_duration,
            selectedMechanismsInfo = selected_mechanisms_info.transform(),
        )
}
