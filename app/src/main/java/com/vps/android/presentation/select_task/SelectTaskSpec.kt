package com.vps.android.presentation.select_task

import android.os.Parcelable
import com.vps.android.domain.mechanism.MechanismItem
import com.vps.android.domain.task.GoodItem
import com.vps.android.domain.task.PlaceItem
import com.vps.android.domain.task.TaskTypeItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectTaskSpec(
    val screenFilter: String,
    val taskTypeItem: TaskTypeItem? = null,
    val loadingPlace: PlaceItem? = null,
    val unloadingPlace: PlaceItem? = null,
    val goodItem: GoodItem? = null,
    val taskTechList: List<MechanismItem>? = null,
) : Parcelable
