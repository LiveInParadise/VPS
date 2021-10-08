package com.vps.android.domain.task

import android.os.Parcelable
import com.vps.android.core.recycler.RecyclerViewItem
import com.vps.android.domain.mechanism.MechanismItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskInfo(
    val id: Int,
    val taskTypeId: Int,
    val taskTypeName: String,
    val workerId: Int,
    val workerName: String,
    val mechanismTypeId: Int,
    val mechanismTypeName: String,
    val mechanismId: Int,
    val mechanismName: String,
    val selectedMechanisms: List<Int>?,
    val goodsId: Int,
    val goodsName: String,
    val loadingPlaceId: Int,
    val loadingPlaceName: String,
    val unloadingPlaceId: Int,
    val unloadingPlaceName: String,
    val distance: Int?,
    val startTime: String?,
    val endTime: String?,
    val date: String?,
    val active: Int,
    val delegatedByMechanismId: Int?,
    val delegatedByTaskId: Int?,
    val isDelegated: Int,
    val mechanismPropertyValue: Int,
    val archived: Int,
    val taskDuration: String?,
    val selectedMechanismsInfo: List<MechanismItem>,
) : Parcelable, RecyclerViewItem {

    override fun getId() = id

    fun isActive() = active == 1

    fun getTaskType() = TaskTypeItem(
        id = taskTypeId,
        name = taskTypeName,
        mechanismTypeId = mechanismTypeId
    )

    fun getLoadingPlace() = PlaceItem(
        id = loadingPlaceId,
        name = loadingPlaceName,
    )

    fun getUnLoadingPlace() = PlaceItem(
        id = unloadingPlaceId,
        name = unloadingPlaceName,
    )

    fun getGoodItem() = GoodItem(
        id = goodsId,
        name = goodsName,
    )
}
