package com.vps.android.presentation.task

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.mechanism.MechanismTypeClass
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeClass
import com.vps.android.domain.task.TaskTypeItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddTaskSpec(
    val taskId: Int? = null,
    val mechanismTypeClass: MechanismTypeClass,
    val taskType: TaskTypeItem? = null,
    val loadingPlace: PlaceItem? = null,
    val unloadingPlace: PlaceItem? = null,
    val goodItem: GoodItem? = null,
    val mechanismItemList: List<MechanismItem>? = null,
    val taskTypeClass: TaskTypeClass,
) : Parcelable
